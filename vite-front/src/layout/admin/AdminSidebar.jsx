import { useDispatch, useSelector } from "react-redux";
import { NavLink, useLocation } from "react-router-dom";

const AdminSidebar = () => {
  const rqStatus = useSelector((state) => state.adminSlice.requestStatus);

  return (
    <aside className="admin-sidebar">
      <ul>
        <li>
          <NavLink to="/admin/index">📊 대시보드</NavLink>
        </li>

        <li>
          <NavLink to="/admin/memberList">👤 멤버 관리</NavLink>
        </li>

        <li>
          <NavLink to="/admin/crewList">👥 크루 관리</NavLink>
        </li>
        <li className="hasPendingApproval">
          <NavLink to="/admin/crewAllow">
            📝 크루개설 승인
            {rqStatus === true ? <span /> : null}
          </NavLink>
        </li>

        <li>
          <NavLink to="/admin/boardList">🗂 커뮤니티 관리</NavLink>
        </li>

        <li>
          <NavLink to="/admin/noticeList">🎉 공지목록 관리</NavLink>
        </li>
        <li>
          <NavLink to="/admin/addNotice">➕ 공지등록</NavLink>
        </li>

        <li>
          <NavLink to="/admin/itemList">🛒 상품목록 관리</NavLink>
        </li>
        <li>
          <NavLink to="/admin/addItem">➕ 상품등록</NavLink>
        </li>

        <li>
          <NavLink to="/admin/paymentList">💳 결제목록 관리</NavLink>
        </li>
      </ul>
    </aside>
  );
};

export default AdminSidebar;
