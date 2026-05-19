import { useState, useRef, useEffect } from 'react'
import { useNavigate, useParams, useLocation } from 'react-router-dom'
import axios from 'axios'
import Sidebar from '../../../components/sidebar/Sidebar'
import InputField from '../../../components/inputField/InputField'
import Button from '../../../components/button/Button'
import './VehicleEditPage.css'

export default function VehicleEditPage() {
  const navigate = useNavigate()
  const { carId } = useParams()
  const location = useLocation()

  const isEditMode = !!carId

  const [isLoading, setIsLoading] = useState(false)

  const carProfileRef = useRef(null)
  const registrationRef = useRef(null)

  const [textData, setTextData] = useState({
    carNum: '',
    vehicleType: '',
    comment: '',
  })

  const [fileData, setFileData] = useState({
    carProfile: null,
    registration: null,
  })

  useEffect(() => {
    const vehicleData = location.state?.vehicleData

    if (isEditMode) {
      if (vehicleData) {
        setTextData({
          carNum: vehicleData.plateNumber || '',
          vehicleType: vehicleData.type || '',
          comment: vehicleData.note || '',
        })
      } else {
        alert('잘못된 접근이거나 차량 정보를 불러올 수 없습니다. 목록으로 돌아갑니다.')
        navigate('/settings', { replace: true })
      }
    }
  }, [carId, location.state, navigate])

  const handleTextChange = (e) => {
    const { name, value } = e.target
    setTextData((prev) => ({ ...prev, [name]: value }))
  }

  const handleFileChange = (e) => {
    const { name, files } = e.target
    if (files && files[0]) {
      setFileData((prev) => ({ ...prev, [name]: files[0] }))
      alert(
        `[${name === 'carProfile' ? '차량 사진' : '차량 등록증'}] 첨부되었습니다: ${files[0].name}`,
      )
    }
  }

  const handleSaveVehicle = async () => {
    if (!textData.carNum || !textData.vehicleType) {
      alert('차량 번호와 차종을 입력해 주세요.')
      return
    }

    if (isLoading) return
    setIsLoading(true)

    try {
      const token = localStorage.getItem('access_token')
      const userId = localStorage.getItem('user_id')
      const API_DOMAIN = ''

      if (!token || !userId) {
        alert('로그인 정보가 없습니다. 다시 로그인해 주세요.')
        setIsLoading(false)
        return
      }

      const formData = new FormData()
      formData.append('carNum', textData.carNum)
      formData.append('vehicleType', textData.vehicleType)
      formData.append('comment', textData.comment)

      if (fileData.carProfile) formData.append('carProfile', fileData.carProfile)
      if (fileData.registration) formData.append('registration', fileData.registration)

      let response

      if (isEditMode) {
        // 수정 (PATCH)
        response = await axios.patch(`${API_DOMAIN}/api/user/car/${carId}`, formData, {
          params: { userId: userId },
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token,
          },
        })
      } else {
        // 추가 (POST)
        response = await axios.post(`${API_DOMAIN}/api/user/cars`, formData, {
          params: { userId: userId },
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: token,
          },
        })
      }

      alert(
        response.data.message ||
          (isEditMode ? '차량 정보 수정이 완료되었습니다.' : '차량 등록이 완료되었습니다.'),
      )
      navigate('/settings')
    } catch (error) {
      if (error.response) {
        const status = error.response.status
        if (status === 400) {
          alert(error.response.data.message || '올바르지 않은 파일 형식입니다.')
        } else if (status === 404) {
          alert(error.response.data.ERROR || '해당 정보를 찾을 수 없습니다.')
        } else {
          alert(isEditMode ? '차량 수정에 실패했습니다.' : '차량 등록에 실패했습니다.')
        }
      } else {
        alert('서버와 연결할 수 없습니다.')
      }
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className='vehicle-edit'>
      <Sidebar />
      <main className='vehicle-edit__main'>
        <h1 className='vehicle-edit__title'>{isEditMode ? '내 차량 수정' : '내 차량 추가'}</h1>

        <section className='vehicle-edit__section'>
          <div className='vehicle-edit__field'>
            <div className='vehicle-edit__field-label-row'>
              <label className='vehicle-edit__field-label'>차량 번호</label>
            </div>
            <InputField
              type='text'
              name='carNum'
              placeholder='10라 7777'
              value={textData.carNum}
              onChange={handleTextChange}
            />
          </div>

          <div className='vehicle-edit__field'>
            <div className='vehicle-edit__field-label-row'>
              <label className='vehicle-edit__field-label'>차종</label>
            </div>
            <InputField
              type='text'
              name='vehicleType'
              placeholder='포르쉐 911'
              value={textData.vehicleType}
              onChange={handleTextChange}
            />
          </div>

          <div className='vehicle-edit__field'>
            <div className='vehicle-edit__field-label-row'>
              <label className='vehicle-edit__field-label'>코멘트</label>
              <span className='vehicle-edit__field-optional'>(선택)</span>
            </div>
            <InputField
              type='text'
              name='comment'
              placeholder='신차입니다. 스크래치 없습니다.'
              value={textData.comment}
              onChange={handleTextChange}
            />
          </div>
        </section>

        <div className='vehicle-edit__divider' />

        <section className='vehicle-edit__section'>
          <div className='vehicle-edit__upload-row'>
            <div className='vehicle-edit__upload-label'>
              <h3 className='vehicle-edit__field-label'>차량 사진</h3>
              <p className='vehicle-edit__upload-desc'>JPG, PNG 파일만 업로드 가능해요</p>
            </div>
            <input
              type='file'
              name='carProfile'
              accept='image/*'
              ref={carProfileRef}
              style={{ display: 'none' }}
              onChange={handleFileChange}
            />
            <Button variant='outline' onClick={() => carProfileRef.current.click()}>
              {isEditMode ? '새 사진 업로드' : '사진 업로드'}
            </Button>
          </div>

          <div className='vehicle-edit__divider' />

          <div className='vehicle-edit__regist-section'>
            <div className='vehicle-edit__upload-row vehicle-edit__regist-row'>
              <div className='vehicle-edit__upload-label'>
                <h3 className='vehicle-edit__field-label'>차량 등록증</h3>
                <p className='vehicle-edit__upload-desc'>PDF, JPG, PNG 파일만 업로드 가능해요</p>
              </div>
              <input
                type='file'
                name='registration'
                accept='.pdf, image/*'
                ref={registrationRef}
                style={{ display: 'none' }}
                onChange={handleFileChange}
              />
              <Button variant='outline' onClick={() => registrationRef.current.click()}>
                {isEditMode ? '새 파일 업로드' : '파일 업로드'}
              </Button>
            </div>
            <div className='vehicle-edit__verified-notice'>
              인증 시 차량에 인증 마크가 표시돼요!
            </div>
          </div>
        </section>

        <div className='vehicle-edit__divider' />

        <div className='vehicle-edit__buttons'>
          <Button variant='ghost' onClick={() => navigate(-1)} disabled={isLoading}>
            취소
          </Button>
          <Button variant='primary' onClick={handleSaveVehicle} disabled={isLoading}>
            {isLoading ? '처리 중...' : isEditMode ? '수정 완료' : '등록 완료'}
          </Button>
        </div>
      </main>
    </div>
  )
}
