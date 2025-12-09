// 날짜를 이쁘게 변환해줌
// 2025. 11. 19. 오후 12:05
// 필요하신분 사용하십쇼
export const formatDate = (dateStr) => {
  const d = new Date(dateStr);
  return d.toLocaleString("ko-KR", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

// 12000 -> 12,000 콤마 붙여줌
export const formattedPrice = (num) => {
  const price = new Intl.NumberFormat("ko-KR").format(num);
  return price;
};

export function sliceDateOnly(timestamp) {
  return timestamp.slice(0, 10);
}
