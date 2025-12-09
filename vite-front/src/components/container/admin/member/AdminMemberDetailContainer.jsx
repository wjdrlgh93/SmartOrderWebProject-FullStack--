import React, { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router";
import jwtAxios from "../../../../apis/util/jwtUtil";
import { BACK_BASIC_URL } from "../../../../apis/commonApis";
import { useSelector } from "react-redux";

const AdminMemberDetailContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const { memberId } = useParams();
  const [gender, setGender] = useState("남성");
  const [memberDto, setMemberDto] = useState({ userName: "" });
  const [updateMemberDto, setUpdateMemberDto] = useState({});

  const navigate = useNavigate();

  const adminMemberDetailFn = async () => {
    try {
      const rs = await jwtAxios.get(
        `${BACK_BASIC_URL}/api/admin/member/detail/${memberId}`,
        {
          headers: { Authorization: `Bearer ${accessToken}` },
          withCredentials: true,
        }
      );
      setMemberDto({ ...rs.data });
      if (rs.data.gender != "MAN") {
        setGender("여성");
      }
    } catch (err) {
      console.log("에러가 발생했습니다. -> admin memberDetail " + err);
    }
  };

  useEffect(() => {
    adminMemberDetailFn();
  }, []);

  useEffect(() => {
    if (memberDto) {
      setUpdateMemberDto({
        userName: memberDto.userName || "",
        nickName: memberDto.nickName || "",
        address: memberDto.address || "",
        age: memberDto.age || "",
        phone: memberDto.phone || "",
      });
    }
  }, [memberDto]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMemberDto((el) => ({ ...el, [name]: value }));
  };

  let jsonData = JSON.stringify(updateMemberDto);

  const adminMemberUpdateFn = async (e) => {
    e.preventDefault();

    try {
      await jwtAxios.put(
        `${BACK_BASIC_URL}/api/admin/member/update/${memberId}`,
        jsonData,
        {
          headers: {
            Authorization: `Bearer ${accessToken}`,
            "Content-Type": "application/json",
          },
        }
      );
      alert("수정이 완료되었습니다.");
    } catch (err) {
      console.log("업데이트 오류: " + err);
    }
  };

  return (
    <>
      <div className="admin-memberDetail">
        <h2 className="admin-memberDetail-title">회원상세정보</h2>

        <div className="admin-memberDetail-container">
          {/* 왼쪽: 프로필 이미지 & 읽기 전용 정보 */}
          <div className="admin-memberDetail-left">
            <div className="admin-profile-section">
              {memberDto.profileImagesList != null ? (
                <img
                  className="admin-profile-image"
                  src={memberDto.fileUrl}
                  alt="프로필 이미지"
                  onError={(e) => {
                    e.target.src =
                      "https://dummyimage.com/200x200/cccccc/000000&text=No+Image";
                  }}
                />
              ) : (
                <img
                  className="admin-profile-image"
                  src="https://dummyimage.com/200x200/cccccc/000000&text=No+Image"
                  alt="기본 프로필"
                />
              )}
            </div>

            <div className="admin-readonly-info">
              <div className="admin-info-field">
                <label className="admin-field-label">이메일</label>
                <div className="admin-field-value-readonly">
                  {memberDto.userEmail}
                </div>
              </div>

              <div className="admin-info-field">
                <label className="admin-field-label">성별</label>
                <div className="admin-field-value-readonly">{gender}</div>
              </div>
            </div>
          </div>

          {/* 오른쪽: 수정 가능한 정보 */}
          <div className="admin-memberDetail-right">
            <div className="admin-edit-form">
              <div className="admin-form-field">
                <label className="admin-field-label">이름</label>
                <input
                  type="text"
                  className="admin-field-input"
                  name="userName"
                  value={memberDto.userName}
                  onChange={handleChange}
                />
              </div>

              <div className="admin-form-field">
                <label className="admin-field-label">닉네임</label>
                <input
                  type="text"
                  className="admin-field-input"
                  name="nickName"
                  value={memberDto.nickName}
                  onChange={handleChange}
                />
              </div>

              <div className="admin-form-field">
                <label className="admin-field-label">주소</label>
                <input
                  type="text"
                  className="admin-field-input"
                  name="address"
                  value={memberDto.address}
                  onChange={handleChange}
                />
              </div>

              <div className="admin-form-field">
                <label className="admin-field-label">전화번호</label>
                <input
                  type="text"
                  className="admin-field-input"
                  name="phone"
                  value={memberDto.phone}
                  onChange={handleChange}
                />
              </div>

              <div className="admin-form-field">
                <label className="admin-field-label">나이</label>
                <input
                  type="number"
                  className="admin-field-input"
                  name="age"
                  value={memberDto.age}
                  onChange={handleChange}
                />
              </div>
            </div>
          </div>

          {/* 하단 버튼 영역 */}
          <div className="admin-memberDetail-bottom">
            <Link to="/admin/memberList" className="admin-back-link">
              ← 목록으로
            </Link>
            <button
              type="submit"
              className="admin-btn-update"
              onClick={adminMemberUpdateFn}
            >
              수정
            </button>
            <button type="button" className="admin-btn-delete">
              탈퇴
            </button>
          </div>
        </div>
      </div>
    </>
  );
};

export default AdminMemberDetailContainer;
