import { NavLink, useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import './Sidebar.css'

import chatActive from '../../assets/sidebar/chat-active.svg'
import chatDefault from '../../assets/sidebar/chat-default.svg'
import profileActive from '../../assets/sidebar/profile active.svg'
import profileDefault from '../../assets/sidebar/profile-default.svg'
import searchActive from '../../assets/sidebar/search-active.svg'
import searchDefault from '../../assets/sidebar/search-default.svg'
import logout from '../../assets/sidebar/logout.svg'

const Sidebar = () => {
  const location = useLocation()
  const navigate = useNavigate()

  const isProfileActive = location.pathname === '/settings' || location.pathname === '/vehicle-edit'

  const handleLogout = () => {
    localStorage.removeItem('user_id')
    localStorage.removeItem('user_email')
    localStorage.removeItem('user_nickname')
    localStorage.removeItem('user_message')
    localStorage.removeItem('user_profile')

    alert('로그아웃 되었습니다.')
    navigate('/login')
  }

  const handleChatClick = async (e) => {
    e.preventDefault()
    try {
      const userId = Number(localStorage.getItem('user_id'))
      const response = await axios.get('/api/chats/top', {
        params: { userId },
      })
      const chats = response.data.chats || []
      if (chats.length > 0) {
        const first = chats[0]
        navigate('/chat', {
          state: {
            chatId: first.chatId,
            carNum: first.carNum,
            nickname: first.owner?.nickName || '',
            userId: first.owner?.userId,
            isVerified: first.registerCar,
          },
        })
      } else {
        navigate('/chat')
      }
    } catch {
      navigate('/chat')
    }
  }

  return (
    <nav className='sidebar'>
      <div className='sidebar__col'>
        <div className='sidebar__top'>
          <NavLink to='/settings'>
            {() => <img src={isProfileActive ? profileActive : profileDefault} alt='프로필' />}
          </NavLink>

          <button onClick={handleChatClick}>
            <img src={location.pathname === '/chat' ? chatActive : chatDefault} alt='채팅' />
          </button>

          <NavLink to='/'>
            {({ isActive }) => <img src={isActive ? searchActive : searchDefault} alt='검색' />}
          </NavLink>
        </div>

        <div className='sidebar__bottom'>
          <div className='sidebar__divider' />
          <button onClick={handleLogout}>
            <img src={logout} alt='로그아웃' />
          </button>
        </div>
      </div>
    </nav>
  )
}

export default Sidebar
