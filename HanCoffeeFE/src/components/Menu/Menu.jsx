import React, { useContext, useState } from 'react'
import './Menu.scss'
import { StoreContext } from '../../context/StoreContext'
const Menu = ({category,setCategory}) => {

  const url = "http://localhost:8888"
  const { category_list } = useContext(StoreContext)

  return (
    <div className='menu' id='menu'>
      <h1 className='menu__heading'>explore our categorys</h1>
      <p className='menu__desc'>
        Embark on a journey through our curated selection of charming cafés. Each spot offers a unique ambiance and exquisite coffee blends. It's more than a cup of coffee; it's a space for connection, creativity, and cherished moments.
      </p>
      <div className="menu__list">
        {category_list.map((item, index) => {
          return (
            <div onClick={() => setCategory(prev => prev === item._id ? 'All' : item._id)} key={index} className="menu__list-item">
              <img src={ item.image} alt="Menu image" className="menu__list-item-img" />
              <h3 className={category === item._id ? "menu__list-item-name active" : "menu__list-item-name"}>{item.name}</h3>
            </div>
          )

          
        })}
      </div>

    </div>
  )
}

export default Menu
