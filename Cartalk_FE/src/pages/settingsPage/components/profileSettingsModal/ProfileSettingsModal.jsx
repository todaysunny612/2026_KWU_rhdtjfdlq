import { useState, useRef } from 'react'
import axios from 'axios'
import InputField from '../../../../components/inputField/InputField'
import Button from '../../../../components/button/Button'
import './ProfileSettingsModal.css'

export default function ProfileSettingsModal({ initialData, onClose, onSuccess }) {
  const [isLoading, setIsLoading] = useState(false)

  // 파일 탐색기를 열기
  const fileInputRef = useRef(null)

  // 폼 상태
  const [formData, setFormData] = useState({
    nickName: initialData?.nickName || '',
    message: initialData?.message || '',
    profile: initialData?.profile || '',
  })

  // 텍스트 입력 감지
  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  // 사진 업로드 클릭 시 동작
  const handlePhotoUploadClick = () => {
    fileInputRef.current.click()
  }

  // 파일 선택 완료 시 동작
  const handleSaveProfile = async () => {
    if (isLoading) return
    setIsLoading(true)

    try {
      const token = localStorage.getItem('access_token')

      const API_DOMAIN = ''

      const response = await axios.patch(
        `/api/user/profile/${initialData.email}`,
        {
          // Request Body
          nickName: formData.nickName,
          message: formData.message,
          profile: formData.profile,
        },
        {
          // Header
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
        },
      )

      alert(response.data.message || '프로필 수정이 완료되었습니다.')
      onSuccess(formData)
    } catch (error) {
      // 에러 메시지 처리
      if (error.response?.status === 404) {
        alert(error.response.data.ERROR || '해당 정보를 찾을 수 없습니다.')
      } else {
        alert('프로필 수정에 실패했습니다.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  // 파일 첨부를 감지하는 함수 추가
  const handleFileChange = (e) => {
    const { files } = e.target
    if (files && files[0]) {
      console.log('파일이 첨부되었습니다:', files[0].name)
      setFormData((prev) => ({ ...prev, profile: files[0].name }))
    }
  }

  return (
    <div className='profile-modal__overlay'>
      <div className='profile-modal'>
        {/* 헤더 */}
        <div className='profile-modal__header'>
          <h2 className='profile-modal__title'>프로필 설정</h2>
          <button className='profile-modal__close' type='button' onClick={onClose}>
            ✕
          </button>
        </div>

        {/* 사진 업로드 영역 */}
        <div className='profile-modal__photo-row'>
          <div className='profile-modal__photo-label'>
            <p className='profile-modal__photo-title'>프로필 사진</p>
            <p className='profile-modal__photo-desc'>PDF, PNG, JPG 파일만 업로드 가능해요</p>
          </div>

          {/* 화면에 보이지 않는 실제 파일 업로드 인풋 */}
          <input
            type='file'
            accept='.pdf, .png, .jpg, .jpeg'
            style={{ display: 'none' }}
            ref={fileInputRef}
            onChange={handleFileChange}
          />

          {/* 사진 업로드 버튼 */}
          <Button variant='outline' onClick={handlePhotoUploadClick}>
            사진 업로드
          </Button>
        </div>

        {/* 구분선 */}
        <div className='profile-modal__divider' />

        {/* 인풋 필드 */}
        <div className='profile-modal__fields'>
          <div className='profile-modal__field'>
            <label className='profile-modal__field-label'>닉네임</label>
            <InputField
              id='profile-nickname'
              type='text'
              name='nickName'
              placeholder='닉네임 입력'
              value={formData.nickName}
              onChange={handleChange}
            />
          </div>
          <div className='profile-modal__field'>
            <label className='profile-modal__field-label'>상태메세지</label>
            <InputField
              id='profile-status'
              type='text'
              name='message'
              placeholder='상태메세지 입력'
              value={formData.message}
              onChange={handleChange}
            />
          </div>
        </div>

        {/* 하단 버튼 */}
        <div className='profile-modal__buttons'>
          <Button variant='ghost' onClick={onClose} disabled={isLoading}>
            취소
          </Button>
          <Button variant='primary' onClick={handleSaveProfile} disabled={isLoading}>
            {isLoading ? '저장 중...' : '저장'}
          </Button>
        </div>
      </div>
    </div>
  )
}
