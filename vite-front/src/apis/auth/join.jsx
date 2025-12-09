import axios from "axios";
import { BACK_BASIC_URL } from "../commonApis";

export const joinFn = async (memberDto, imgFile) => {
  const formData = new FormData();
  formData.append(
    "memberDto",
    new Blob([JSON.stringify(memberDto)], { type: "application/json" })
  );
  formData.append("memberFile", imgFile);

  let res;
  try {
    res = await axios.post(`${BACK_BASIC_URL}/api/member/join`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    return res;
  } catch (err) {
    console.log("회원가입 실패");
    return err;
  }
};
