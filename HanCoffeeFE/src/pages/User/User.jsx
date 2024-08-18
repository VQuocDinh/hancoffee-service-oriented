import React from 'react'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import './User.css'
import { faBell, faList, faUser, faWarehouse } from '@fortawesome/free-solid-svg-icons'

const UserMenu = () => {
  const menuItems = [
    { icon: faUser, text: 'Thông tin tài khoản' },
    { icon: faBell, text: 'Thông báo' },
    { icon: faList, text: 'Đơn mua' },
    { icon: faWarehouse, text: 'Kho voucher' }
  ]

  return (
    <div className="user__select">
      <ul className="user__select-list">
        {menuItems.map((item, index) => (
          <li key={index} className="user__select-list-item">
            <FontAwesomeIcon icon={item.icon} />
            {item.text}
          </li>
        ))}
      </ul>
    </div>
  )
}

const UserInfor = () => {
  const inforItems = [
    { label: 'Tên', type: 'text' },
    { label: 'Số điện thoại', type: 'text' },
    { label: 'Ngày sinh', type: 'text' },
    { label: 'Email', type: 'text' },
  ]

  return (
    <div className="user__content">
      <div className="user__content-heading">
        <h2>Thông tin tài khoản</h2>
      </div>

      <div className="user__content-body">
        {inforItems.map((item, index) => (
          <div className="user__content-body-item">
            <label htmlFor={item.label}>{item.label}</label>
            <input id={item.label} type={item.type} className="content__body-item-input" aria-label={item.label} />
          </div>
        ))}

        {/* <div className="user__content-body-sex">
          <span>Giới tính</span>
          <div className="user__content-body-sex-select">
            <label>
              Nam
              <input type='radio' className="content__body-sex-input" />
            </label>

            <label>
              Nữ
              <input type='radio' className="content__body-sex-input" />
            </label>
          </div>
        
        </div> */}
      </div>

      <div className="user__content-body-update">
        <button className="user__content-body-update-btn">Cập nhật</button>
      </div>

    </div>
  )
}

const User = () => {
  return (
    <div className='user'>
      <UserMenu />
      <UserInfor />
    </div>
  )
}

export default User
