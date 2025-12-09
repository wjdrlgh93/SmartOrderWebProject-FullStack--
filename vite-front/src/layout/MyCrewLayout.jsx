import React, { useState } from 'react'
import Header from '../components/common/Header'
import { Outlet } from 'react-router-dom'
import Footer from '../components/common/Footer'
import MyCrewLeftContainer from '../components/container/myCrew/MyCrewLeftContainer'

const MyCrewLayout = () => {
  const [isLeftOpen, setIsLeftOpen] = useState(false);

  const onLeft = () => setIsLeftOpen(prev => !prev);
  const closeLeft = () => setIsLeftOpen(false);
  return (
    

    <div className="mycrew">
      <div className="mycrew-con">
        <div
          className={`mycrew-left-dim ${isLeftOpen ? "show" : ""}`}
          onClick={closeLeft}/>
        <div className={`left ${isLeftOpen ? "is-open" : ""}`}>
          <MyCrewLeftContainer />
        </div>
        <div className="right">
          <Header/> 
          <Outlet/>
            <button type="button" className="mycrew-menu-btn" onClick={onLeft}>
              ☰ 메뉴</button>
        </div>

      </div>
    </div>
    
  )
}

export default MyCrewLayout