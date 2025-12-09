import React, { useState } from "react";

const AdminPagingComponent = ({ pageData, onPageChange }) => {
  const currentPage = pageData.currentPage || 1;
  const totalPages = pageData.totalPages || 1;

  const pageNumbers = Array.from({ length: totalPages }, (_, i) => i + 1);

  const handlePageClick = (currentPage) => {
    return onPageChange(currentPage);
  };

  console.log(currentPage);

  return (
    <div className="admin-paging">
      <button
        disabled={currentPage === 1}
        onClick={() => handlePageClick(currentPage - 1)}
      >
        이전
      </button>

      {pageNumbers.map((page) => (
        <button
          key={page} // React에서 반복 요소에는 반드시 고유한 key가 필요합니다!
          className={page === currentPage ? "active" : ""}
          onClick={() => handlePageClick(page)}
        >
          {page}
        </button>
      ))}
      <button
        disabled={currentPage === totalPages}
        onClick={() => handlePageClick(currentPage + 1)}
      >
        다음
      </button>
    </div>
  );
};

export default AdminPagingComponent;
