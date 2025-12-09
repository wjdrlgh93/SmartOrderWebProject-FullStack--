import React, { useState } from "react";
import { useNavigate } from "react-router";
import { joinFn } from "../../../apis/auth/join";
import axios from "axios";
import { BACK_BASIC_URL } from "../../../apis/commonApis";

const AuthJoinContainer = () => {
  const navigate = useNavigate();
  const [errors, setErrors] = useState({});
  const [emailCheck, setEmailCheck] = useState({
    data: "",
    status: "",
  });
  const [imgFile, setImgFile] = useState(null);
  const [memberDto, setMemberDto] = useState({
    userEmail: "",
    userPassword: "",
    userName: "",
    nickName: "",
    gender: "MAN",
    age: "",
    phone: "",
  });

  console.log(memberDto);

  console.log(emailCheck);
  console.log(errors);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setMemberDto((el) => ({ ...el, [name]: value }));
  };

  const emailCheckFn = async (value) => {
    setErrors((prev) => ({
      ...prev,
      userEmail: "",
    }));
    try {
      const res = await axios.get(
        `${BACK_BASIC_URL}/api/member/email/check?email=${value}`
      );
      setEmailCheck((prev) => ({
        ...prev,
        data: res.data,
        status: res.status,
      }));
      console.log(res);
    } catch (err) {
      setEmailCheck((prev) => ({
        ...prev,
        data: err.response.data,
        status: err.status,
      }));
      console.log(err);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setEmailCheck("");

    const res = await joinFn(memberDto, imgFile);
    console.log(res);
    if (res.status === 200) {
      alert("회원가입 완료!, 로그인 페이지로 이동합니다.");
      navigate("/auth/login");
    } else if (res.response.status === 400) {
      setErrors(res.response.data);
    } else {
      alert("회원가입 실패했습니다. 다시 시도해주세요.");
    }
  };

  return (
    <div className="member-join">
      <div className="member-join-con">
        <h2>회원가입</h2>
        <form onSubmit={handleSubmit}>
          <ul>
            <li className="profile-image-field">
              <label htmlFor="imgFile">프로필 이미지</label>
              <div className="file-input-wrapper">
                <input
                  type="file"
                  id="imgFile"
                  onChange={(e) => {
                    const file = e.target.files[0];
                    setImgFile(file);
                    if (file) {
                      const reader = new FileReader();
                      reader.onload = (event) => {
                        document.querySelector(".preview-image").src =
                          event.target.result;
                        document.querySelector(".preview-image").style.display =
                          "block";
                        document.querySelector(".file-label").textContent =
                          file.name;
                        document.querySelector(
                          ".preview-container"
                        ).style.display = "block";
                      };
                      reader.readAsDataURL(file);
                    }
                  }}
                  accept="image/*"
                />
                <span className="file-label">파일 선택</span>
              </div>
              <div className="preview-container" style={{ display: "none" }}>
                <img className="preview-image" alt="미리보기" />
                <button
                  type="button"
                  className="delete-image-btn"
                  onClick={() => {
                    setImgFile(null);
                    document.getElementById("imgFile").value = "";
                    document.querySelector(".preview-container").style.display =
                      "none";
                    document.querySelector(".file-label").textContent =
                      "파일 선택";
                  }}
                >
                  ✕
                </button>
              </div>
            </li>

            <li>
              <label htmlFor="userEmail">이메일</label>
              <div className="input-with-button">
                <input
                  type="email"
                  name="userEmail"
                  id="userEmail"
                  placeholder="이메일을 입력해주세요"
                  onChange={handleChange}
                />
                <button
                  type="button"
                  className="check-duplicate-btn"
                  onClick={() => {
                    const email = document.getElementById("userEmail").value;
                    emailCheckFn(email);
                  }}
                >
                  중복확인
                </button>
              </div>
              {errors.userEmail && (
                <p className="error-message">{errors.userEmail}</p>
              )}
              {emailCheck.status == 200 ? (
                <p className="email-message">{emailCheck.data}</p>
              ) : (
                <p className="error-message">{emailCheck.data}</p>
              )}
            </li>

            <li>
              <label htmlFor="userPassword">비밀번호</label>
              <input
                type="password"
                name="userPassword"
                id="userPassword"
                placeholder="비밀번호 4자리 이상 입력해주세요"
                onChange={handleChange}
              />
              {errors.userPassword && (
                <p className="error-message">{errors.userPassword}</p>
              )}
            </li>

            <li>
              <label htmlFor="userName">이름</label>
              <input
                type="text"
                name="userName"
                id="userName"
                placeholder="이름을 입력해주세요"
                onChange={handleChange}
              />
              {errors.userName && (
                <p className="error-message">{errors.userName}</p>
              )}
            </li>

            <li>
              <label htmlFor="nickName">닉네임</label>
              <input
                type="text"
                name="nickName"
                id="nickName"
                placeholder="닉네임을 입력해주세요"
                onChange={handleChange}
              />
              {errors.nickName && (
                <p className="error-message">{errors.nickName}</p>
              )}
            </li>

            <li className="member-gender">
              <label className="genderLabel">성별</label>
              <div className="member-gender-list">
                <label className="radio-label">
                  <input
                    type="radio"
                    name="gender"
                    value="MAN"
                    onChange={handleChange}
                    defaultChecked
                  />
                  <span>남자</span>
                </label>
                <label className="radio-label">
                  <input
                    type="radio"
                    name="gender"
                    value="WOMAN"
                    onChange={handleChange}
                  />
                  <span>여자</span>
                </label>
              </div>
            </li>

            <li>
              <label htmlFor="address">주소</label>
              <input
                type="text"
                name="address"
                id="address"
                placeholder="주소를 입력해주세요"
                onChange={handleChange}
              />
              {errors.address && (
                <p className="error-message">{errors.address}</p>
              )}
            </li>

            <li>
              <label htmlFor="age">나이</label>
              <input
                type="number"
                name="age"
                id="age"
                placeholder="나이를 입력해주세요"
                onChange={handleChange}
                min="1"
                max="150"
              />
              {errors.age && <p className="error-message">{errors.age}</p>}
            </li>

            <li>
              <label htmlFor="phone">전화번호</label>
              <input
                type="tel"
                name="phone"
                id="phone"
                placeholder="전화번호를 입력해주세요"
                onChange={handleChange}
              />
              {errors.phone && <p className="error-message">{errors.phone}</p>}
            </li>

            <li className="submit-button-wrapper">
              <button type="submit">회원가입</button>
            </li>
          </ul>
        </form>
      </div>
    </div>
  );
};

export default AuthJoinContainer;
