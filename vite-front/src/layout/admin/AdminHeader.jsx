import React from "react";
import { Link } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import LogoutBtn from "../../components/common/LogoutBtn";

const AdminHeader = () => {
  const userEmail = useSelector((state) => state.loginSlice.userEmail);

  return (
    <header className="admin-header">
      <div className="admin-header-left">
        <Link to="/admin/index" className="logo">
          ADMIN
        </Link>
      </div>

      <div className="admin-header-right">
        <ul>
          <li>{userEmail}</li>
          <li>
            <Link to="/store">SHOP</Link>
          </li>
          <LogoutBtn />
        </ul>
      </div>
    </header>
  );
};

export default AdminHeader;
