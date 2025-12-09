import React, { useState } from 'react'

import "../../../css/common/headerModal.css"
import { Link } from 'react-router';

const HeaderStore = ({ children }) => {




     return (

          <div className="modal-container" >
               {children}

               <div className="modal-box">
                    <ul>
                         <li>
                              <Link to="/store/index">스토어메인</Link>
                         </li>
                         <li>
                              <Link to="/store/shoes">슈즈</Link>
                         </li>
                         <li>
                              <Link to="/store/cloth">의류</Link>
                         </li>
                         <li>
                              <Link to="/store/equipment">장비</Link>
                         </li>
                         <li>
                              <Link to="/store/accessory">악세사리</Link>
                         </li>
                         <li>
                              <Link to="/store/nutrition">영양</Link>
                         </li>

                    </ul>
               </div>
          </div>

     )
}

export default HeaderStore