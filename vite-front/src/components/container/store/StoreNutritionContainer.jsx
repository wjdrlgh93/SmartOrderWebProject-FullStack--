import axios from 'axios';
import React, { useEffect, useState } from 'react'
import { Link } from 'react-router';

import "../../../css/store/storeAcce.css"

const ShopNutritionContainer = () => {

  const NO_IMAGE_URL = "/images/noimage.jpg";


  const [items, setItems] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({
    totalPages: 0,
    startPage: 0,
    endPage: 0,
    currentPage: 0
  })
  const displayPageNum = 5; // 화면에 표시할 페이지 버튼 개수

  const fetchData = async (page) => {

    // const response = await axios.get("http://localhost:8088/api/shop");
    const response = await axios.get(`http://localhost:8088/api/shop/nutrition?page=${page}`);
    const data = response.data;
    console.log(`[LOG] 페이지 ${page + 1}의 데이터를 요청합니다.`);

    try {
      if (data && data.content) {

        setItems(data.content || []);

        // 페이지 정보 계산 및 업데이트
        const totalPages = data.totalPages;
        const pageNum = data.number;
        const displayPageNum = 5;

        const startPage = Math.floor(pageNum / displayPageNum) * displayPageNum;
        let endPage = startPage + displayPageNum - 1;
        if (endPage >= totalPages) {
          endPage = totalPages - 1;
        }
        setPageInfo({
          currentPage: pageNum,
          totalPages: totalPages,
          startPage: startPage,
          endPage: endPage,
        });
      }
    } catch (error) {
      console.error("상품 데이터 로드 실패:", error);
      setItems([]);
    }
  };

  useEffect(() => {
    fetchData(currentPage);
  }, [currentPage]);

  const pageNumbers = [];
  for (let i = pageInfo.startPage; i <= pageInfo.endPage; i++) {
    pageNumbers.push(i);
  }
  const handlePageClick = (page) => {
    // 페이지 변경 요청 시 스크롤을 맨 위로 이동 (사용자 경험 개선)
    window.scrollTo({ top: 0, behavior: 'smooth' });
    setCurrentPage(page);
  }






  return (
    <div className="itemList">
      <br />

      <div className="itemList-banner">

      </div>
      {/* i guess i can insert later => here Pagingnation Effect... */}
      <div className="itemList-con">
        <h2> 🥗🥦 영양만점 건강아이템 🥗🥦</h2>
        <br />
        <div className='item-grid-container'>
          {items.length === 0 && <p className="no-items-data">등록된 상품이 없습니다.</p>}
          {console.log(items)}

          {items.map(list => (
            <Link to={`/store/detail/${list.id}`} key={list.id} className='item-card-link'>
              <div className="item-card">

                {/* 상품 이미지 영역 */}
                {list.attachFile === 1 ? (
                  <div className="item-image-placeholder">
                    {list.itemImgDtos.map((imgDto, index) => (
                      <img
                        key={index} 
                        src={imgDto.fileUrl} 
                        alt={imgDto.oldName}
                        className="gallery-image"
                    />
                    ))}
                  </div>
                ) : (
                  <img
                    src={NO_IMAGE_URL}
                    alt="이미지 없음"
                    width="250"
                    height="250"
                    className="item-image"
                  />
                )}
                <span className="no-image-text"></span>
                <div className="item-info">
                  <h4 className='item-title'>{list.itemTitle}</h4>
                  <p className='item-price'>
                    {list.itemPrice ? list.itemPrice.toLocaleString() : '가격 미정'} 원
                  </p>
                </div>
              </div>
            </Link>
          ))}
        </div>
        {/* E.O.F item-grid-container */}

        {/* Paging Section */}
        {pageInfo.totalPages > 1 && (
          <div className="pagination-container">

            {/* 이전 페이지 블록으로 이동 (<) */}
            <button
              className="page-btn page-prev"
              onClick={() => handlePageClick(pageInfo.startPage - 1)}
              disabled={pageInfo.startPage === 0}
            >
              &lt;
            </button>

            {/* 페이지 번호 버튼들 */}
            {pageNumbers.map((page) => (
              <button
                key={page}
                className={`page-btn ${page === currentPage ? 'active' : ''}`}
                onClick={() => handlePageClick(page)}
              >
                {page + 1}
              </button>
            ))}

            {/* 다음 페이지 블록으로 이동 (>) */}
            <button
              className="page-btn page-next"
              onClick={() => handlePageClick(pageInfo.endPage + 1)}
              disabled={pageInfo.endPage >= pageInfo.totalPages - 1}
            >
              &gt;
            </button>

          </div>
        )}
        {/* E.O.F pagination-container */}

      </div>
    </div>
  )
}

export default ShopNutritionContainer