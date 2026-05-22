import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import Sidebar from '../../../components/sidebar/Sidebar'
import ChatListItem from '../components/chatListItem/ChatListItem'
import SearchNoneModal from '../components/searchNoneModal/SearchNoneModal'
import AccidentGuide from '../components/accidentGuide/AccidentGuide'
import logoSrc from '../../../assets/logo/CarTALK.svg'
import './SearchPage.css'

const api = axios.create({
  baseURL: '',
  withCredentials: true,
  headers: { 'content-type': 'application/json' },
})

export default function SearchPage() {
  const [searchValue, setSearchValue] = useState('')
  const [IS_SEARCH_NONE, SET_IS_SEARCH_NONE] = useState(false)
  const [isLoading, setIsLoading] = useState(false)
  const [recentChats, setRecentChats] = useState([])

  const navigate = useNavigate()

  // [API 2] 최근 채팅 목록 불러오기
  useEffect(() => {
    const fetchRecentChats = async () => {
      try {
        const userId = Number(localStorage.getItem('user_id'))
        const response = await api.get('/api/chats/top', {
          params: { userId: userId },
        })
        setRecentChats(response.data.chats || [])
      } catch (error) {
        console.error('최근 채팅 목록 로드 실패:', error)
        setRecentChats([])
      }
    }

    fetchRecentChats()
  }, [])

  // [API 1] 차량번호 검색 핸들러
  const handleSearch = async () => {
    if (!searchValue.trim() || isLoading) return

    setIsLoading(true)
    SET_IS_SEARCH_NONE(false)

    try {
      const response = await api.get('/api/cars', {
        params: { carNum: searchValue },
      })

      const { carNum, owner } = response.data

      const myId = Number(localStorage.getItem('user_id'))
      if (owner.userId === myId) {
        alert('내 차량은 채팅할 수 없습니다.')
        setIsLoading(false)
        return
      }

      navigate('/chat', {
        state: {
          userId: owner.userId,
          carNum: carNum,
          nickname: owner.nickName,
          avatarUrl: owner.profile,
          isVerified: owner.registerCar,
        },
      })
    } catch (error) {
      if (error.response && error.response.status === 404) {
        SET_IS_SEARCH_NONE(true)
      } else {
        console.error('차량 검색 중 에러 발생:', error)
      }
    } finally {
      setIsLoading(false)
    }
  }

  const handleKeyDown = (e) => {
    if (e.key === 'Enter') handleSearch()
  }

  return (
    <div className='search'>
      <Sidebar />

      <main className='search__main'>
        {/* 헤더 — 로고 + 검색바 */}
        <div className='search__header'>
          <img className='search__logo' src={logoSrc} alt='CarTALK' />

          <div className='search__bar'>
            <input
              className='search__input'
              type='text'
              placeholder='차량 번호를 검색하세요'
              value={searchValue}
              onChange={(e) => setSearchValue(e.target.value)}
              onKeyDown={handleKeyDown}
            />
            <button className='search__btn' onClick={handleSearch} disabled={isLoading}>
              {isLoading ? '...' : '검색'}
            </button>
          </div>
        </div>

        <div className='search__divider' />

        {/* 최근 채팅 목록 */}
        <div className='search__recent'>
          <h2 className='search__recent-title'>최근 채팅 목록</h2>
          <div className='search__list'>
            {recentChats.length > 0 ? (
              recentChats.map((chat) => (
                <ChatListItem
                  key={chat.chatId}
                  plateNumber={chat.carNum}
                  lastMessage={chat.lastMessage || '아직 대화가 없어요'}
                  isVerified={chat.registerCar}
                  avatarUrl={chat.owner?.profile}
                  onClick={() =>
                    navigate('/chat', {
                      state: {
                        chatId: chat.chatId,
                        carNum: chat.carNum,
                        nickname: chat.owner?.nickName || '',
                        userId: chat.owner?.userId,
                        isVerified: chat.registerCar,
                      },
                    })
                  }
                />
              ))
            ) : (
              <p className='search__no-chats'>최근 채팅이 없어요</p>
            )}
          </div>
        </div>

        <div className='search__divider' />

        {/* 교통사고 대처요령 */}
        <div className='search__guide'>
          <h2 className='search__recent-title'>교통사고 대처요령</h2>
          <AccidentGuide />
        </div>
      </main>

      {IS_SEARCH_NONE && (
        <>
          <div
            style={{ position: 'fixed', inset: 0, zIndex: 199 }}
            onClick={() => SET_IS_SEARCH_NONE(false)}
          />
          <SearchNoneModal onClose={() => SET_IS_SEARCH_NONE(false)} />
        </>
      )}
    </div>
  )
}
