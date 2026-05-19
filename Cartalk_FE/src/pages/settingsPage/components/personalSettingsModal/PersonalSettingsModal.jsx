import { useState } from 'react'
import axios from 'axios'
import InputField from '../../../../components/inputField/InputField'
import Button from '../../../../components/button/Button'
import './PersonalSettingsModal.css'

export default function PersonalSettingsModal({ onClose }) {
  const [isLoading, setIsLoading] = useState(false)

  const [formData, setFormData] = useState({
    name: '',
    phoneNumber: '',
  })

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))
  }

  const handleSavePersonal = async () => {
    if (!formData.name || !formData.phoneNumber) {
      alert('이름과 전화번호를 모두 입력해 주세요.')
      return
    }

    if (isLoading) return
    setIsLoading(true)

    try {
      const token = localStorage.getItem('access_token')
      const email = localStorage.getItem('user_email')
      const API_DOMAIN = ''

      if (!token || !email) {
        alert('로그인 정보가 없습니다. 다시 로그인해 주세요.')
        return
      }

      const response = await axios.patch(
        `/api/auth/account/${email}`,
        {
          name: formData.name,
          phoneNumber: formData.phoneNumber,
        },
        {
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
        },
      )

      alert(response.data.message || '내 정보가 설정 되었습니다.')
      onClose()
    } catch (error) {
      if (error.response && error.response.status === 400) {
        alert(error.response.data.message || '입력 형식이 올바르지 않습니다.')
      } else {
        alert('정보 설정에 실패했습니다. 잠시 후 다시 시도해 주세요.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className='personal-modal__overlay'>
      <div className='personal-modal'>
        <div className='personal-modal__header'>
          <h2 className='personal-modal__title'>내 정보 설정</h2>
          <button className='personal-modal__close' type='button' onClick={onClose}>
            ✕
          </button>
        </div>

        <div className='personal-modal__fields'>
          <div className='personal-modal__field'>
            <label className='personal-modal__field-label'>이름</label>
            <InputField
              id='personal-name'
              type='text'
              name='name'
              placeholder='예: 양의지'
              value={formData.name}
              onChange={handleChange}
            />
          </div>

          <div className='personal-modal__field'>
            <label className='personal-modal__field-label'>전화번호</label>
            <InputField
              id='personal-phone'
              type='text'
              name='phoneNumber'
              placeholder='예: 010-1234-5678'
              value={formData.phoneNumber}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className='personal-modal__buttons'>
          <Button variant='ghost' onClick={onClose} disabled={isLoading}>
            취소
          </Button>
          <Button variant='primary' onClick={handleSavePersonal} disabled={isLoading}>
            {isLoading ? '저장 중...' : '저장'}
          </Button>
        </div>
      </div>
    </div>
  )
}
