import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { authDetailFn, authUpdateFn } from "../../../apis/auth/authDetail";
import { BACK_BASIC_URL } from "../../../apis/commonApis";
import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";

const AuthUpdateContainer = () => {
  const [gender, setGender] = useState("남성");
  const [imgFile, setImgFile] = useState(null);
  const [isImageDeleted, setIsImageDeleted] = useState(false);
  const [memberDto, setMemberDto] = useState({
    id: "",
    userEmail: "",
    userPassword: "",
    userName: "",
    nickName: "",
    gender: "",
    age: "",
    phone: "",
  });

  const memberId = useSelector((state) => state.loginSlice.id);
  const navigate = useNavigate();

  const myPageDetailFn = async () => {
    const res = await authDetailFn();
    if (res.status === 200) {
      setMemberDto({ ...res.data });
      if (res.data.gender === "WOMAN") {
        setGender("여성");
      }
    }
  };

  const memberUpdateFn = async (e) => {
    e.preventDefault();
    const res = await authUpdateFn(memberDto, imgFile);
    console.log(res);
    if (res.status === 200) {
      alert("수정이 완료되었습니다.");
      navigate("/myPage");
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMemberDto((el) => ({ ...el, [name]: value }));
  };

  const handleImageDelete = () => {
    setImgFile(null);
    setIsImageDeleted(true);
    document.getElementById("profileImageInput").value = "";
    document.querySelector(".profile-image").src =
      "https://dummyimage.com/150x150/cccccc/000000&text=No+Image";
    document.querySelector(".file-name").textContent = "";
    document.querySelector(".file-name").style.display = "none";
  };

  useEffect(() => {
    myPageDetailFn();
  }, []);

  return (
    <div className="member-update">
      <div className="member-update-con">
        <h2>회원정보 수정</h2>
        <form onSubmit={memberUpdateFn}>
          <main className="member-update-right">
            <div className="profile-section">
              <span className="profile-label">프로필 이미지</span>
              <div className="profile-image-wrapper">
                {memberDto.profileImagesList != null && !isImageDeleted ? (
                  <div className="profile-image-container">
                    <img
                      className="profile-image"
                      src={memberDto.fileUrl}
                      alt="프로필 이미지"
                      onError={(e) => {
                        e.target.src =
                          "https://dummyimage.com/150x150/cccccc/000000&text=No+Image";
                      }}
                    />
                  </div>
                ) : (
                  <div className="profile-image-container">
                    <img
                      className="profile-image"
                      src="https://dummyimage.com/150x150/cccccc/000000&text=No+Image"
                      alt="기본 프로필"
                    />
                  </div>
                )}
                <div className="file-input-wrapper">
                  <input
                    type="file"
                    id="profileImageInput"
                    onChange={(e) => {
                      const file = e.target.files[0];
                      setImgFile(file);
                      setIsImageDeleted(false);
                      if (file) {
                        const reader = new FileReader();
                        reader.onload = (event) => {
                          document.querySelector(".profile-image").src =
                            event.target.result;
                          document.querySelector(".file-name").textContent =
                            file.name;
                          document.querySelector(".file-name").style.display =
                            "block";
                        };
                        reader.readAsDataURL(file);
                      }
                    }}
                    accept="image/*"
                  />
                  <div className="file-button-group">
                    <label
                      htmlFor="profileImageInput"
                      className="file-select-btn"
                    >
                      이미지 변경
                    </label>
                    <button
                      type="button"
                      className="file-delete-btn"
                      onClick={handleImageDelete}
                    >
                      이미지 삭제
                    </button>
                  </div>

                  <span
                    className="file-name"
                    style={{ display: "none" }}
                  ></span>
                </div>
              </div>
            </div>

            <div className="info-section">
              <div className="info-item readonly">
                <label className="info-label">이메일</label>
                <div className="info-value">{memberDto.userEmail}</div>
              </div>

              <div className="info-item">
                <label htmlFor="userName" className="info-label">
                  이름
                </label>
                <input
                  type="text"
                  id="userName"
                  name="userName"
                  value={memberDto.userName}
                  onChange={handleChange}
                  placeholder="이름을 입력해주세요"
                />
              </div>

              <div className="info-item">
                <label htmlFor="userPassword" className="info-label">
                  비밀번호
                </label>
                <input
                  type="password"
                  id="userPassword"
                  name="userPassword"
                  onChange={handleChange}
                  placeholder="변경할 비밀번호를 입력해주세요"
                />
                <p className="input-hint">
                  * 비밀번호를 변경하지 않으려면 비워두세요
                </p>
              </div>

              <div className="info-item">
                <label htmlFor="nickName" className="info-label">
                  닉네임
                </label>
                <input
                  type="text"
                  id="nickName"
                  name="nickName"
                  value={memberDto.nickName}
                  onChange={handleChange}
                  placeholder="닉네임을 입력해주세요"
                />
              </div>

              <div className="info-item">
                <label htmlFor="address" className="info-label">
                  주소
                </label>
                <input
                  type="text"
                  id="address"
                  name="address"
                  value={memberDto.address}
                  onChange={handleChange}
                  placeholder="주소를 입력해주세요"
                />
              </div>

              <div className="info-item">
                <label htmlFor="phone" className="info-label">
                  전화번호
                </label>
                <input
                  type="tel"
                  id="phone"
                  name="phone"
                  value={memberDto.phone}
                  onChange={handleChange}
                  placeholder="전화번호를 입력해주세요"
                />
              </div>

              <div className="info-item">
                <label htmlFor="age" className="info-label">
                  나이
                </label>
                <input
                  type="number"
                  id="age"
                  name="age"
                  value={memberDto.age}
                  onChange={handleChange}
                  placeholder="나이를 입력해주세요"
                  min="1"
                  max="150"
                />
              </div>

              <div className="info-item readonly">
                <label className="info-label">성별</label>
                <div className="info-value">{gender}</div>
              </div>
            </div>

            <div className="button-group">
              <button
                className="btn-cancel"
                type="button"
                onClick={() => window.history.back()}
              >
                취소
              </button>
              <button className="btn-edit" type="submit">
                수정하기
              </button>
            </div>
          </main>
        </form>
      </div>
    </div>
  );
};

export default AuthUpdateContainer;
