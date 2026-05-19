import { useState, useEffect, useRef } from 'react'
import { useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import Sidebar from '../../../components/sidebar/Sidebar'
import ChatHeader from '../components/chatHeader/ChatHeader'
import ChatBubble from '../components/chatBubble/ChatBubble'
import ChatInputBar from '../components/chatInputBar/ChatInputBar'
import VehicleModal from '../components/vehicleModal/VehicleModal'
import SafeCallModal from '../components/safeCallModal/SafeCallModal'
import CallRestrictModal from '../components/callRestrictModal/CallRestrictModal'
import chatNoneIcon from '../../../assets/chat/chat-none.svg'
import './ChatPage.css'

const api = axios.create({
  baseURL: '',
  withCredentials: true,
  headers: { 'content-type': 'application/json' },
})

export default function ChatPage() {
  const { state } = useLocation()
  const navigate = useNavigate()

  const [isVehicleModalOpen, setIsVehicleModalOpen] = useState(false)
  const [isSafeCallModalOpen, setIsSafeCallModalOpen] = useState(false)
  const [IS_CALL_RESTRICT_OPEN, SET_IS_CALL_RESTRICT_OPEN] = useState(false)
  const [inputValue, setInputValue] = useState('')
  const [isChatActive, setIsChatActive] = useState(true)

  const [chatId, setChatId] = useState(null)
  const [messages, setMessages] = useState([])
  const [NEXT_CURSOR, SET_NEXT_CURSOR] = useState(null)
  const [HAS_NEXT, SET_HAS_NEXT] = useState(false)
  const [isRoomLoading, setIsRoomLoading] = useState(true)
  const [isSending, setIsSending] = useState(false)

  const [maxCount, setMaxCount] = useState(null)
  const [remainingCount, setRemainingCount] = useState(null)
  const [isCallAvailable, setIsCallAvailable] = useState(null)

  const messagesEndRef = useRef(null)

  const scrollToBottom = (behavior = 'smooth') => {
    messagesEndRef.current?.scrollIntoView({ behavior })
  }

  useEffect(() => {
    if (messages.length > 0) {
      scrollToBottom('smooth')
    }
  }, [messages])

  useEffect(() => {
    if (!state || !state.userId) {
      setIsChatActive(false)
      setIsRoomLoading(false)
    }
  }, [state, navigate])

  // [API 1] 채팅방 생성/조회
  useEffect(() => {
    const getOrCreateChatRoom = async () => {
      if (!state?.userId) return

      setIsRoomLoading(true)
      try {
        const response = await api.post('/api/chats', {
          targetUserId: state.userId, // userId 제거 완료
        })
        setChatId(response.data.chatId)
      } catch (error) {
        console.error('채팅방 생성에 실패했습니다.', error)
        navigate('/')
      } finally {
        setIsRoomLoading(false)
      }
    }

    getOrCreateChatRoom()
  }, [state, navigate])

  // [API 2] 메시지 조회
  const fetchMessages = async () => {
    if (!chatId || isSending) return

    try {
      const response = await api.get(`/api/chats/${chatId}/messages`, {
        params: { limit: 30 }, // userId 파라미터 제거 완료
      })

      const fetchedMessages = response.data.messages || []
      setMessages([...fetchedMessages].reverse())
      SET_NEXT_CURSOR(response.data.nextCursor)
      SET_HAS_NEXT(response.data.hasNext)

      setTimeout(() => scrollToBottom('auto'), 50)
    } catch (error) {
      console.error('메시지 조회 실패:', error)
      setMessages([])
    }
  }

  // 최초 1회 + 2초마다 반복
  useEffect(() => {
    fetchMessages()
    const interval = setInterval(fetchMessages, 2000)
    return () => clearInterval(interval)
  }, [chatId])

  // [API 3] 텍스트 메시지 전송
  const handleSend = async () => {
    if (!inputValue.trim() || isSending || !chatId) return

    const currentText = inputValue
    setInputValue('')
    setIsSending(true)

    const optimisticMessage = {
      messageId: Date.now(),
      nickname: '나',
      content: currentText,
      messageType: 'TEXT',
      createdAt: new Date().toISOString(),
      mine: true,
    }

    setMessages((prev) => [...prev, optimisticMessage])

    try {
      await api.post(`/api/chats/${chatId}/messages`, {
        content: currentText,
        messageType: 'TEXT',
      })
    } catch (error) {
      console.error('메시지 전송 실패:', error)
      alert('메시지 전송에 실패했습니다.')
    } finally {
      setIsSending(false)
    }
  }

  //[API 4] 이미지 메시지 전송 핸들러
  const handleImageSend = async (file) => {
    if (!file || !chatId) return

    const formData = new FormData()
    formData.append('image', file)

    const previewUrl = URL.createObjectURL(file)
    const optimisticImage = {
      messageId: Date.now(),
      messageType: 'IMAGE',
      content: previewUrl,
      mine: true,
      createdAt: new Date().toISOString(),
    }
    setMessages((prev) => [...prev, optimisticImage])

    try {
      const response = await api.post('/api/images/chat', formData, {
        headers: { 'content-type': 'multipart/form-data' },
      })
      const imageUrl = response.data.imageUrl

      setMessages((prev) =>
        prev.map((msg) => (msg.content === previewUrl ? { ...msg, content: imageUrl } : msg)),
      )
    } catch (error) {
      console.error('이미지 전송 실패:', error)
      alert('이미지 전송에 실패했습니다.')
      setMessages((prev) => prev.filter((msg) => msg.content !== previewUrl))
    }
  }

  // [API 5] 채팅 완료
  const handleComplete = async () => {
    if (!chatId) return

    if (window.confirm('정말 대화를 완료하고 채팅방을 종료하시겠습니까?')) {
      try {
        await api.delete(`/api/chats/${chatId}`)
        setIsChatActive(false)
      } catch (error) {
        console.error('채팅 완료 처리 실패:', error)
      }
    }
  }

  // 안심전화 버튼 클릭 핸들러
  const handleSafeCall = () => {
    if (isCallAvailable === false || (remainingCount !== null && remainingCount === 0)) {
      SET_IS_CALL_RESTRICT_OPEN(true)
      return
    }
    setIsSafeCallModalOpen(true)
  }

  // 안심전화 요청 API 핸들러
  const handleConfirmSafeCall = async () => {
    if (!chatId) return

    try {
      const response = await api.post(`/api/chats/${chatId}/calls`)

      const { call, callAvailable } = response.data

      if (!callAvailable) {
        setIsSafeCallModalOpen(false)
        SET_IS_CALL_RESTRICT_OPEN(true)
        return
      }

      setMaxCount(call.maxCount)
      setRemainingCount(call.remainingCount)
      setIsCallAvailable(callAvailable)
      setIsSafeCallModalOpen(false)
    } catch (error) {
      console.error('안심전화 발신 실패:', error)
      setIsSafeCallModalOpen(false)
      SET_IS_CALL_RESTRICT_OPEN(true)
    }
  }

  if (isRoomLoading) {
    return (
      <div className='chat'>
        <Sidebar />
        <div className='chat__none'>
          <div className='chat__none-inner'>
            <p className='chat__none-line'>채팅방을 안전하게 불러오는 중입니다...</p>
          </div>
        </div>
      </div>
    )
  }

  if (!isChatActive) {
    return (
      <div className='chat'>
        <Sidebar />
        <div className='chat__none'>
          <div className='chat__none-inner'>
            <img className='chat__none-icon' src={chatNoneIcon} alt='' />
            <div className='chat__none-text'>
              <p className='chat__none-line'>아직 대화가 없어요</p>
              <p className='chat__none-line'>검색에서 차량을 찾아 채팅을 시작해보세요</p>
            </div>
          </div>
        </div>
      </div>
    )
  }

  return (
    <div className='chat'>
      <Sidebar />

      <div className='chat__main'>
        <ChatHeader
          plateNumber={state?.carNum || '차량번호 없음'}
          nickname={state?.nickname || '닉네임 정보 없음'}
          isVerified={true}
          safeCallCount={
            remainingCount !== null && maxCount !== null ? `${remainingCount}/${maxCount}` : '-/-'
          }
          onAvatarClick={() => setIsVehicleModalOpen(true)}
          onSafeCall={handleSafeCall}
          onComplete={handleComplete}
        />

        <div className='chat__room'>
          {messages.map((msg) => (
            <ChatBubble
              key={msg.messageId}
              type={msg.mine ? 'mine' : 'other'}
              text={msg.messageType === 'TEXT' ? msg.content : undefined}
              imageUrl={msg.messageType === 'IMAGE' ? msg.content : undefined}
            />
          ))}
          <div ref={messagesEndRef} />
        </div>

        <ChatInputBar
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          onSend={handleSend}
          onImageSelect={handleImageSend}
        />
      </div>

      {isVehicleModalOpen && (
        <VehicleModal
          plateNumber={state?.carNum || '12가 3456'}
          nickname={state?.nickname || '닉네임은드라이버'}
          type='현대 아반떼 CN7'
          note='원래 범퍼에 스크래치가 있습니다'
          status='주차 중이에요'
          isVerified={true}
          onClose={() => setIsVehicleModalOpen(false)}
        />
      )}

      {isSafeCallModalOpen && (
        <SafeCallModal
          onConfirm={handleConfirmSafeCall}
          onClose={() => setIsSafeCallModalOpen(false)}
        />
      )}

      {IS_CALL_RESTRICT_OPEN && (
        <CallRestrictModal onClose={() => SET_IS_CALL_RESTRICT_OPEN(false)} />
      )}
    </div>
  )
}
