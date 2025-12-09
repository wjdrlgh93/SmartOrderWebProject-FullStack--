import React, { useState } from "react";
import Header from "../components/common/Header";
import { Outlet, useNavigate } from "react-router";
import Footer from "../components/common/Footer";

const MyPageLayout = () => {
  const currentTab = location.pathname.includes("payment")
    ? "결제정보"
    : "회원정보";
  const navigate = useNavigate();
  return (
    <>
      <Header />
      <div className="memberDetail-background">
        <div className="memberDetail">
          <h1>MyPage</h1>
          <div className="memberDetail-con">
            <aside className="memberDetail-left">
              <nav>
                <ul>
                  <li
                    className={currentTab === "회원정보" ? "active" : ""}
                    onClick={() => {
                      navigate("/myPage");
                    }}
                  >
                    회원정보
                  </li>
                  <li
                    className={currentTab === "결제정보" ? "active" : ""}
                    onClick={() => {
                      navigate("/myPage/payment");
                    }}
                  >
                    결제정보
                  </li>
                </ul>
              </nav>
            </aside>
            <Outlet />
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default MyPageLayout;
