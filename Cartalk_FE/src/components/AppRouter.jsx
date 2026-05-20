import { createBrowserRouter } from 'react-router-dom'
import LoginPage from '../pages/loginPage/entry/LoginPage'
import SignupPage from '../pages/signupPage/entry/SignupPage'
import SettingsPage from '../pages/settingsPage/entry/SettingsPage'
import VehicleEditPage from '../pages/vehicleEditPage/entry/VehicleEditPage'
import SearchPage from '../pages/searchPage/entry/SearchPage'
import ChatPage from '../pages/chatPage/entry/ChatPage'

// 로그인 여부 확인 후 보호된 페이지 렌더링
function PrivateRoute({ element }) {
  const userId = localStorage.getItem('user_id')
  return userId ? element : <Navigate to='/login' replace />
}

const AppRouter = createBrowserRouter([
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/signup',
    element: <SignupPage />,
  },
  {
    path: '/',
    element: <SearchPage />,
  },
  {
    path: '/settings',
    element: <SettingsPage />,
  },
  {
    path: '/vehicle-edit',
    element: <VehicleEditPage />,
  },
  {
    path: '/chat',
    element: <ChatPage />,
  },
])

export default AppRouter
