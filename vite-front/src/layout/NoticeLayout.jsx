import Header from "../components/common/Header";
import { Outlet } from "react-router-dom";
import Footer from "../components/common/Footer";

const NoticeLayout = () => {
  return (
    <>
      <Header />
      <Outlet />
      <Footer />
    </>
  );
};

export default NoticeLayout;
