import { useState, useRef } from 'react'
import axios from 'axios'
import InputField from '../../../../components/inputField/InputField'
import Button from '../../../../components/button/Button'
import './ProfileSettingsModal.css'

export default function ProfileSettingsModal({ initialData, onClose, onSuccess }) {
  const [isLoading, setIsLoading] = useState(false)
  const [isUploading, setIsUploading] = useState(false)

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

  // 파일 선택 완료 시 — 바로 서버에 업로드 후 URL 저장
  const handleFileChange = async (e) => {
    const { files } = e.target
    if (!files || !files[0]) return

    const file = files[0]
    setIsUploading(true)

    try {
      const uploadForm = new FormData()
      uploadForm.append('image', file)

      const response = await axios.post('/api/images/chat', uploadForm, {
        headers: { 'Content-Type': 'multipart/form-data' },
      })

      // 업로드된 이미지 URL을 profile에 저장
      setFormData((prev) => ({ ...prev, profile: response.data.imageUrl }))
      alert('사진이 업로드되었습니다.')
    } catch (error) {
      console.error('사진 업로드 실패:', error)
      alert('사진 업로드에 실패했습니다.')
    } finally {
      setIsUploading(false)
    }
  }

  console.log('initialData:', initialData)
  console.log('email:', initialData.email)
  const handleSaveProfile = async () => {
    if (isLoading) return
    setIsLoading(true)

    try {
      const response = await axios.patch(
        `/api/user/profile/${initialData.email}`,
        {
          // Request Body
          nickName: formData.nickName,
          message: formData.message,
          profile: formData.profile,
        },
        {
          headers: { 'Content-Type': 'application/json' },
        },
      )

      alert(response.data.message || '프로필 수정이 완료되었습니다.')
      onSuccess(formData)
    } catch (error) {
      console.log('에러 상태코드:', error.response?.status)
      console.log('에러 메시지:', error.response?.data)
      console.log('요청 URL:', `/api/user/profile/${initialData.email}`)
      console.log('요청 데이터:', {
        nickName: formData.nickName,
        message: formData.message,
        profile: formData.profile,
      })

      if (error.response?.status === 404) {
        alert(error.response.data.ERROR || '해당 정보를 찾을 수 없습니다.')
      } else if (error.response?.status === 400) {
        alert(error.response.data.message || '입력값을 확인해주세요.')
      } else {
        alert('프로필 수정에 실패했습니다.')
      }
    } finally {
      setIsLoading(false)
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
            <p className='profile-modal__photo-desc'>
              {/* 업로드된 파일명 또는 안내 문구 표시 */}
              {formData.profile ? '업로드 완료' : 'PNG, JPG 파일만 업로드 가능해요'}
            </p>
          </div>

          {/* 화면에 보이지 않는 실제 파일 업로드 인풋 */}
          <input
            type='file'
            accept='image/png, image/jpg, image/jpeg'
            style={{ display: 'none' }}
            ref={fileInputRef}
            onChange={handleFileChange}
          />

          {/* 사진 업로드 버튼 */}
          <Button variant='outline' onClick={handlePhotoUploadClick} disabled={isUploading}>
            {isUploading ? '업로드 중...' : '사진 업로드'}
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
          <Button variant='ghost' onClick={onClose} disabled={isLoading || isUploading}>
            취소
          </Button>
          <Button variant='primary' onClick={handleSaveProfile} disabled={isLoading || isUploading}>
            {isLoading ? '저장 중...' : '저장'}
          </Button>
        </div>
      </div>
    </div>
  )
}
