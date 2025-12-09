import { useNavigate } from "react-router";

import { logoutFn } from "../../apis/auth/logout";

const LogoutBtn = () => {
  const navigate = useNavigate();

  const logoutBtnFn = async () => {
    alert("로그아웃 처리되었습니다!");
    await logoutFn();
    navigate("/store/index");
  };

  return (
    <li onClick={logoutBtnFn} className="logout-button">
      LOGOUT
    </li>
  );
};

export default LogoutBtn;
