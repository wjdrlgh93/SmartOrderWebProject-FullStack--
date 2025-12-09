import React, { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import AdminHeader from "./AdminHeader";
import AdminSidebar from "./AdminSidebar";
import Footer from "../../components/common/Footer"; // 기존 footer 재사용
import { useDispatch, useSelector } from "react-redux";
import { newCrewCreateRequestFn } from "../../apis/admin/adminCrewList";
import { hasRequestStatus } from "../../slices/adminSlice";

import "../../css/admin/AdminLayout.css";
import "../../css/admin/AdminHeader.css";
import "../../css/admin/AdminSidebar.css";
import "../../css/common/footer.css";

const AdminLayout = () => {
  const role = useSelector((state) => state.loginSlice.role);
  const navigate = useNavigate();
  const dispatch = useDispatch();

  useEffect(() => {
    if (role !== "ADMIN") {
      alert("접근 권한이 없습니다.");
      navigate("/");
      return;
    }
  }, [role]);

  const crewRequestList = async () => {
    const res = await newCrewCreateRequestFn(1, "");
    const boolRs = res.data.content.some((el) => el.status === "PENDING");
    dispatch(hasRequestStatus(boolRs));
  };

  useEffect(() => {
    crewRequestList();
  }, []);

  return (
    <div className="admin-layout">
      <AdminHeader />

      <div className="admin-container">
        <div className="admin-container-left">
          <AdminSidebar />
        </div>

        <main className="admin-container-right">
          <Outlet />
        </main>
      </div>

      {/* <Footer /> */}
    </div>
  );
};

export default AdminLayout;
