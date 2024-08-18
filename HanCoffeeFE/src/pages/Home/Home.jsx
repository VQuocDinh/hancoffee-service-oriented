import React, { useState } from 'react'
import './Home.css'
import Header from '../../components/Header/Header'
import Menu from '../../components/Menu/Menu'
import Product from '../../components/Product/Product'
import Download from '../../components/Download/Download'
const Home = () => {
  const [category, setCategory] = useState("All")
  return (
    <div className='content'>
      <Header />
      <Menu category = {category} setCategory = {setCategory} />
      <Product category={category} />
      <Download />
    </div>
  )
}

export default Home
