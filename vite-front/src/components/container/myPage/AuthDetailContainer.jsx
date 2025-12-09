import React, { useEffect, useState } from "react";
import { authDeleteFn, authDetailFn } from "../../../apis/auth/authDetail";
import { useSelector } from "react-redux";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import { useNavigate } from "react-router";
import AuthDeleteModal from "./AuthDeleteModal";

const AuthDetailContainer = () => {
  const [memberDetail, setMemberDetail] = useState({});
  const [crewMemberList, setCrewMemberList] = useState([]);
  const [gender, setGender] = useState("남성");
  const [isModal, setIsModal] = useState(false);

  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const navigate = useNavigate();

  useEffect(() => {
    if (accessToken == null) {
      alert("로그인 후 접속해주세요.");
      navigate("/auth/login");
    }
    const myPageDetailFn = async () => {
      const res = await authDetailFn();
      const authDetail = res.data;

      console.log(res.data);

      if (res.status === 200) {
        setMemberDetail({ ...authDetail });
        setCrewMemberList(authDetail.crewMemberEntityList);
        if (authDetail.gender === "WOMAN") {
          setGender("여성");
        }
      }
    };
    myPageDetailFn();
  }, []);

  const modalHandler = () => {
    setIsModal(true);
  };

  const updatePage = () => {
    navigate("/myPage/update");
  };

  return (
    <main className="memberDetail-right">
      <div className="profile-section">
        {memberDetail.profileImagesList != null ? (
          <div className="profile-image">
            <span className="profile-label">프로필 이미지</span>
            <img
              src={memberDetail.fileUrl}
              alt="프로필 이미지"
              onError={(e) => {
                e.target.src =
                  "https://dummyimage.com/150x150/cccccc/000000&text=No+Image";
              }}
            />
          </div>
        ) : (
          <div className="profile-image">
            <span className="profile-label">프로필 이미지</span>
            <img
              src="https://dummyimage.com/150x150/cccccc/000000&text=No+Image"
              alt="기본 프로필"
            />
          </div>
        )}
      </div>

      <div className="member-detail-info-section">
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">이메일</span>
          <span className="member-detail-info-value">
            {memberDetail.userEmail}
          </span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">이름</span>
          <span className="member-detail-info-value">
            {memberDetail.userName}
          </span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">닉네임</span>
          <span className="member-detail-info-value">
            {memberDetail.nickName}
          </span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">주소</span>
          <span className="member-detail-info-value">
            {memberDetail.address}
          </span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">전화번호</span>
          <span className="member-detail-info-value">{memberDetail.phone}</span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">나이</span>
          <span className="member-detail-info-value">{memberDetail.age}세</span>
        </div>
        <div className="member-detail-info-item">
          <span className="member-detail-info-label">성별</span>
          <span className="member-detail-info-value">{gender}</span>
        </div>
      </div>

      <div className="button-group">
        <button className="btn-edit" onClick={updatePage}>
          수정
        </button>
        <button className="btn-delete" onClick={modalHandler}>
          탈퇴
        </button>
        {isModal ? (
          <AuthDeleteModal
            setIsModal={setIsModal}
            crewMemberList={crewMemberList}
          />
        ) : null}
      </div>
    </main>
  );
};

export default AuthDetailContainer;
