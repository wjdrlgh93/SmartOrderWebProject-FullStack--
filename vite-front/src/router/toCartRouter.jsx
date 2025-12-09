import React, { lazy, Suspense } from 'react';

const Loading = <div className='loading'>Loading..</div>;
const CartPage = lazy(() => import('../components/container/cart/CartPage')); // 확장자 명시

export default function toCartRouter() {
  return [
    {
      path: "", // /cart
      element: (
        <Suspense fallback={Loading}>
          <CartPage />
        </Suspense>
      ),
    },
  ];
}
