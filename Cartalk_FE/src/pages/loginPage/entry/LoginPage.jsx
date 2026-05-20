import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import axios from 'axios'
import Button from '../../../components/button/Button'
import PasswordChangeModal from '../components/passwordChangeModal/PasswordChangeModal'
import logoSrc from '../../../assets/logo/CarTALK.svg'
import './LoginPage.css'

export default function LoginPage() {
  const [isModalOpen, setIsModalOpen] = useState(false)
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')

  const navigate = useNavigate()

  const handleLogin = async (e) => {
    e.preventDefault()

    try {
      const response = await axios.post('/api/user/login', {
        email: email,
        password: password,
      })

      // 로그인 정보 저장
      localStorage.setItem('user_email', response.data.email)
      localStorage.setItem('user_id', response.data.id)
      localStorage.setItem('user_nickname', response.data.userName || '')
      localStorage.setItem('user_message', '')
      localStorage.setItem('user_profile', '')

      alert(response.data.message || '로그인 성공')
      navigate('/')
    } catch (error) {
      if (error.response) {
        const status = error.response.status
        const errorMessage = error.response.data.message

        if (status === 404) {
          alert(errorMessage)
        } else if (status === 401) {
          alert(errorMessage)
        } else {
          alert('로그인 처리 중 문제가 발생했습니다.')
        }
      } else {
        alert('서버와 연결할 수 없습니다.')
      }

      console.error('로그인 에러:', error)
    }
  }

  return (
    <main className='login'>
      <div className='login__wrap'>
        {/* 로고 */}
        <img className='login__brand' src={logoSrc} alt='CarTALK' />

        {/* 로그인 카드 */}
        <section className='login__card' aria-label='로그인'>
          <div className='login__content'>
            <div className='login__body'>
              {/* 언더라인 인풋 묶음 */}
              <div className='login__inputs'>
                <input
                  className='login__input'
                  type='email'
                  placeholder='이메일을 입력하세요'
                  autoComplete='email'
                  required
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                />
                <input
                  className='login__input'
                  type='password'
                  placeholder='비밀번호를 입력하세요'
                  autoComplete='current-password'
                  required
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                />
              </div>

              {/* 버튼 묶음 */}
              <div className='login__buttons'>
                <Button variant='primary' size='md' type='submit' onClick={handleLogin}>
                  로그인
                </Button>

                <Button variant='ghost' size='md' onClick={() => navigate('/signup')}>
                  회원가입
                </Button>
              </div>
            </div>

            <a
              className='login__forgot'
              onClick={() => setIsModalOpen(true)}
              style={{ cursor: 'pointer' }}
            >
              비밀번호를 잊으셨나요?
            </a>
          </div>
        </section>
      </div>

      {isModalOpen && <PasswordChangeModal onClose={() => setIsModalOpen(false)} />}
    </main>
  )
}
