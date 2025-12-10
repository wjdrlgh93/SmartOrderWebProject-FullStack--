import React from "react";
import { useSelector } from "react-redux";
import HeaderStore from "../common/HeaderModal/HeaderStore";
import HeaderCrewList from "../common/crewModal/CrewList";
import { Link } from "react-router-dom";
import LogoutBtn from "./LogoutBtn";


import "../../css/common/header.css";
import BoardModal from "./HeaderModal/BoardModal";


const Header = () => {

  const isLogin = useSelector((state) => state.loginSlice.isLogin);
  const role = useSelector((state) => state.loginSlice.role);
 
  return (
    <div className="header">
      <div className="nav">
        <h1>HOME</h1>
        <div className="gnb">
          <ul>
            {isLogin ? (

              <>
                <LogoutBtn />
                <li>
                  <Link to="/cart">CART</Link>
                </li>

                <li>
                  <Link to="/myPage">myPage</Link>
                </li>
                <HeaderCrewList>
                  <li>
                    <Link to="/crew/list">CREW</Link>
                  </li>
                </HeaderCrewList>
                {role === "ADMIN" ? (
                  <li>
                    <Link to="/admin/index">ADMIN</Link>
                  </li>
                ) : null}
              </>
            ) : (
              <>
                <li>
                  <Link to="/auth/login">LOGIN</Link>
                </li>
                <li>
                  <Link to="/auth/join">JOIN</Link>
                </li>
                <li>
                  <Link to="/crew/list">CREW</Link>
                </li>
              </>
            )}
            <HeaderStore>
              <li>
                <Link to="/store">STORE</Link>
              </li>
            </HeaderStore>
            <BoardModal>
            <li>
              <Link to="/board">BOARD</Link> 
            </li>
            </BoardModal>
       
            <li>
              <Link to="/open/marathon">INFORMATION</Link>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
};

export default Header;
