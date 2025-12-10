


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


export const formattedPrice = (num) => {
  const price = new Intl.NumberFormat("ko-KR").format(num);
  return price;
};

export function sliceDateOnly(timestamp) {
  return timestamp.slice(0, 10);
}
