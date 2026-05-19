import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import Sidebar from '../../../components/sidebar/Sidebar'
import ProfileCard from '../components/profileCard/ProfileCard'
import { VehicleCard, VehicleAddCard } from '../components/vehicleCard/VehicleCard'
import ProfileSettingsModal from '../components/profileSettingsModal/ProfileSettingsModal'
import PersonalSettingsModal from '../components/personalSettingsModal/PersonalSettingsModal'
import './SettingsPage.css'

export default function SettingsPage() {
  const [isProfileModalOpen, setIsProfileModalOpen] = useState(false)
  const [isPersonalModalOpen, setIsPersonalModalOpen] = useState(false)
  const [isVehicleEditing, setIsVehicleEditing] = useState(false)
  const navigate = useNavigate()
  const [profileData, setProfileData] = useState({
    email: '',
    nickName: '',
    message: '',
    profile: '',
    isVerified: false,
  })

  useEffect(() => {
    const fetchMyProfile = async () => {
      try {
        const token = localStorage.getItem('access_token')
        const email = localStorage.getItem('user_email')

        if (!token || !email) return

        const API_DOMAIN = ''

        const response = await axios.get(`${API_DOMAIN}/api/user/profile/${email}`, {
          headers: {
            'Content-Type': 'application/json',
            Authorization: token,
          },
        })

        setProfileData({
          email: email,
          nickName: response.data.nickName,
          message: response.data.message,
          profile: response.data.profile,
          isVerified: response.data.isVerified || false,
        })
      } catch (error) {
        console.error('프로필 정보를 불러오지 못했습니다.', error)
      }
    }

    fetchMyProfile()
  }, [])

  // 차량 목록 조회 api 필요
  const vehicles = []

  const handleVehicleSave = () => {
    setIsVehicleEditing(false)
  }

  return (
    <div className='settings'>
      <Sidebar />

      <main className='settings__main'>
        <h1 className='settings__title'>프로필 설정</h1>

        <ProfileCard
          nickname={profileData.nickName}
          status={profileData.message}
          isVerified={profileData.isVerified}
          onEditProfile={() => setIsProfileModalOpen(true)}
          onEditPersonal={() => setIsPersonalModalOpen(true)}
        />

        <div className='settings__divider' />

        <div className='settings__vehicle-header'>
          <h2 className='settings__vehicle-title'>내 차량</h2>
          {isVehicleEditing ? (
            <button className='settings__vehicle-save' onClick={handleVehicleSave}>
              차량 저장
            </button>
          ) : (
            <button className='settings__vehicle-edit' onClick={() => setIsVehicleEditing(true)}>
              차량 편집
            </button>
          )}
        </div>

        <div className='settings__vehicle-list'>
          {vehicles.map((v) => (
            <VehicleCard
              key={v.id}
              plateNumber={v.plateNumber}
              type={v.type}
              note={v.note}
              isVerified={v.isVerified}
              isEditing={isVehicleEditing}
              onClick={() => navigate('/vehicle-edit')}
            />
          ))}
          {isVehicleEditing && <VehicleAddCard onClick={() => navigate('/vehicle-edit')} />}
        </div>
      </main>

      {/* 모달 연동 부분 (변경 없음) */}
      {isProfileModalOpen && (
        <ProfileSettingsModal
          initialData={profileData}
          onClose={() => setIsProfileModalOpen(false)}
          onSuccess={(updatedData) => {
            setProfileData((prev) => ({ ...prev, ...updatedData }))
            setIsProfileModalOpen(false)
          }}
        />
      )}

      {isPersonalModalOpen && (
        <PersonalSettingsModal onClose={() => setIsPersonalModalOpen(false)} />
      )}
    </div>
  )
}
