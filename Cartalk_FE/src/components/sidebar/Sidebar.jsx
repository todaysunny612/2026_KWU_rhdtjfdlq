import { NavLink, useLocation, useNavigate } from 'react-router-dom'
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
    alert('로그아웃 되었습니다.')
    navigate('/login')
  }

  return (
    <nav className='sidebar'>
      <div className='sidebar__col'>
        <div className='sidebar__top'>
          <NavLink to='/settings'>
            {() => <img src={isProfileActive ? profileActive : profileDefault} alt='프로필' />}
          </NavLink>

          <NavLink to='/chat'>
            {({ isActive }) => <img src={isActive ? chatActive : chatDefault} alt='채팅' />}
          </NavLink>

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
