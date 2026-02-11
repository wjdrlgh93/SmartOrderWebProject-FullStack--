import axios from "axios";
import React, { useEffect, useState } from "react";
import { Link, useNavigate, useSearchParams } from "react-router-dom";

import jwtAxios from "../../../apis/util/jwtUtil";
import { useSelector } from "react-redux";

import "../../../css/crew/crewMain.css";

const CrewMainContainer = () => {
  const accessToken = useSelector((state) => state.jwtSlice.accessToken);
  const [crewList, setCrewList] = useState([]);
  const [myCrewList, setMyCrewList] = useState([]);
  const navigate = useNavigate();

  const [size] = useState(10);
  const [totalPages, setTotalPages] = useState(0);
  const [startPage, setStartPage] = useState(0);
  const [endPage, setEndPage] = useState(0);
  const [hasNext, setHasNext] = useState(false);
  const [hasPrevious, setHasPrevious] = useState(false);

  const [keyword, setKeyword] = useState("");
  const [subject, setSubject] = useState("전체");

  // 페이지 유지
  const [searchParams, setSearchParams] = useSearchParams();
  const [page, setPage] = useState(() => {
    const p = parseInt(searchParams.get("page"));
    return isNaN(p) ? 0 : p;
  });
  const pageChange = (newPage) => {
    setPage(newPage);
    searchParams.set("page", newPage);
    setSearchParams(searchParams);
  };

  useEffect(() => {
    if (accessToken == null) {
      setMyCrewList([]);
    }
  }, []);

  const allCrewList = async () => {
    try {
      const res = await axios.get(`/api/crew/list`, {
        params: { page, size, keyword, subject },
      });
      const data = res.data.crewList;
      setCrewList(data.content);
      setTotalPages(data.totalPages || 0);
      setStartPage(data.startPage);
      setEndPage(data.endPage);
      setHasNext(data.hasNext);
      setHasPrevious(data.hasPrevious);
    } catch (err) {
      console.error("크루 목록 실패", err);
    }
  };

  useEffect(() => {
    allCrewList();
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, [page]);

  const search = (e) => {
    e.preventDefault();
    setCrewList([]);
    setPage(0);
    allCrewList(0);
  };

  // 본인이 가입한 크루 조회
  useEffect(() => {
    jwtAxios
      .get(`/api/mycrew/list`, {
        headers: { Authorization: `Bearer ${accessToken}` },
        withCredentials: true,
      })
      .then((res) => {
        setMyCrewList(res.data.myCrewList);
        console.log(res.data.myCrewList);
      })
      .catch((err) => {
        console.error("내 크루 목록 조회 실패 ", err);
      });
  }, []);

  return (
    <>
      <div className="crewList">
        <div className="kakaomap">{/* insert here kakaoMapAPI */}</div>
        <div className="crewList-con">
          {accessToken != null ? (
            <div className="myCrewList">
              <h1 className="crewListH1">내 크루 목록</h1>
              <ul className="crewListUl">
                {myCrewList.map((crew) => {
                  // const images = crew.crewImages || [];
                  const images = crew.fileUrl || [];
                  console.log(images)
                  return (
                    <li className="crewListLi" key={crew.id}>
                      <Link to={`/mycrew/${crew.crewId}`}>
                        <div className="crewListLeft">
                          {images.length > 0 ? (
                            <img
                              src={images[0]}
                              alt={`${crew.crewName} 이미지`}
                              className="crewImage"
                            />
                          ) : (
                            <div className="noCrewImage">
                              <span>🧑‍🤝‍🧑💨 {crew.crewName} 💨👟</span>
                            </div>
                          )}
                        </div>

                        <div className="crewListRight">
                          <h2>{crew.crewName}</h2>
                          <div className="crewDistrictAndMember">
                            <p>{crew.district}</p>
                            <p>멤버&nbsp;{crew.crewMembers}</p>
                          </div>
                        </div>
                      </Link>
                    </li>
                  );
                })}
              </ul>{" "}
            </div>
          ) : null}
          <div className="allCrewList">
            <h1 className="crewListH1">크루 목록</h1>
            <form className="crewSearch" onSubmit={search}>
              <select
                name="crewBoard"
                id="crewBoard"
                value={subject}
                onChange={(e) => setSubject(e.target.value)}
              >
                <option value="전체">전체</option>
                <option value="크루이름">크루명</option>
                <option value="크루소개">설명</option>
                <option value="지역">지역</option>
              </select>
              <input
                type="text"
                placeholder="검색"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
              <button className="crewSearchBtn" type="submit">
                검색
              </button>
            </form>
            {crewList === null ? (
              <p>존재하는 크루가 없습니다.</p>
            ) : (
            <ul className="crewListUl">
              {crewList.map((crew) => {
                // const images = crew.newFileName || [];
                const images = crew.fileUrl || [];
                console.log(images)
                return (
                  <li className="crewListLi" key={crew.id}>
                    <Link to={`/crew/detail/${crew.id}`}>
                      <div className="crewListLeft">
                        {images && images.length > 0 ? (
                          <img
                            src={images[0]}
                            alt={`${crew.name} 이미지`}
                            className="crewImage"
                          />
                        ) : (
                          <div className="noCrewImage">👟💨 {crew.name} 💨🧑‍🤝‍🧑</div>
                        )}
                      </div>
                      <div className="crewListRight">
                        <h2>{crew.name}</h2>
                        <p>{crew.description && crew.description.length > 40 ?
                          `${crew.description.slice(0, 40)}...` : crew.description}</p>
                        <div className="crewDistrictAndMember">
                          <p>{crew.district}</p>
                          <p>멤버&nbsp;{crew.crewMemberEntities && crew.crewMemberEntities.length}</p>
                        </div>
                      </div>
                    </Link>
                  </li>
                );
              })}
            </ul>
            )}
          </div>
          <div className="crewBoardPagination">
            <button onClick={() => pageChange(0)} disabled={page === 0}>
              처음
            </button>
            <button
              onClick={() => pageChange(page - 1)}
              disabled={!hasPrevious}
            >
              이전
            </button>
            {Array.from({ length: endPage - startPage + 1 }, (_, idx) => (
              <button
                key={idx}
                onClick={() => pageChange(startPage + idx - 1)}
                disabled={startPage + idx - 1 === page}
              >
                {startPage + idx}
              </button>
            ))}
            <button onClick={() => pageChange(page + 1)} disabled={!hasNext}>
              다음
            </button>
            <button
              onClick={() => pageChange(totalPages - 1)}
              disabled={page === totalPages - 1}
            >
              마지막
            </button>
          </div>
          <button onClick={() => navigate(`/crew/createRequest`)}>
            크루만들기
          </button>
        </div>
      </div>
    </>
  );
};

export default CrewMainContainer;
