import jwtAxios from "../util/jwtUtil";

const CART_API = "http://localhost:8088/api/cart";

// === JWT 기반 장바구니 조회 ===
export const getCartByToken = async () => {
  const token = localStorage.getItem("accessToken");
  if (!token) throw new Error("로그인이 필요합니다.");

  const res = await jwtAxios.get(`${CART_API}/me`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return res.data;
};

// === 장바구니 아이템 추가 ===
export const addItemToCart = async (cartId, itemId, itemSize = 1) => {
  const res = await jwtAxios.post(
    `${CART_API}/${cartId}/item`,
    {},
    { params: { itemId, itemSize } }
  );
  return res.data;
};

// === 장바구니 아이템 삭제 ===
export const removeCartItem = async (cartItemId) => {
  await jwtAxios.delete(`${CART_API}/item/${cartItemId}`);
};

// === 장바구니 아이템 수량 변경 (추가) ===
export const updateCartItemQuantity = async (cartItemId, quantity) => {
  const res = await jwtAxios.put(
    `${CART_API}/item/${cartItemId}/quantity`,
    {},
    { params: { quantity } }
  );
  return res.data;
};

// === 장바구니 아이템 페이징 조회 + 검색 ===
export const getCartItemsByPage = async (cartId, page = 0, size = 10, keyword = "") => {
  const res = await jwtAxios.get(`${CART_API}/${cartId}/items`, {
    params: { page, size, keyword },
  });
  return res.data;
};
