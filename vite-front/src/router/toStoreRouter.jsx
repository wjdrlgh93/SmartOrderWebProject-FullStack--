import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

// Containiner
const StoreAcc = lazy(()=> import('../components/container/store/StoreAccessoryContainer'))
const StoreCloth = lazy(()=> import('../components/container/store/StoreClothContainer'))
const StoreEqip = lazy(()=> import('../components/container/store/StoreEquipmentContainer'))
const StoreMain = lazy(()=> import('../components/container/store/StoreMainContainer'))
const StoreNut = lazy(()=> import('../components/container/store/StoreNutritionContainer'))
const StoreSale = lazy(()=> import('../components/container/store/StoreSaleContainer'))
const StoreShoes = lazy(()=> import('../components/container/store/StoreShoesContainer'))
const StoreDetail = lazy(()=> import('../components/container/store/StoreDetailContainer'))

const toStoreRouter = () => {
  return (
    [
        {
            // Store/
            path:'',
            element: <Navigate replace to={'index'} />
        
        },
        {
          // StoreFront
            path: 'index',
            element: <Suspense fallback={Loading}><StoreMain/></Suspense>
        },
        {
                // board/detail
            path:'detail/:id',
            element: <Suspense fallback={Loading}><StoreDetail/></Suspense>
        
        },
        {
            path: 'salezone',
            element: <Suspense fallback={Loading}><StoreSale/></Suspense>
        },
        {
          path:'shoes',
          element: <Suspense fallback={Loading}><StoreShoes/></Suspense>
        },
        {
          path:'cloth',
          element: <Suspense fallback={Loading}><StoreCloth/></Suspense>
        },
        {
          path:'equipment',
          element: <Suspense fallback={Loading}><StoreEqip/></Suspense>
        },
        {
          path:'accessory',
          element: <Suspense fallback={Loading}><StoreAcc/></Suspense>
        },
        {
          path:'nutrition',
          element: <Suspense fallback={Loading}><StoreNut/></Suspense>
        }
    ]
  )
}

export default toStoreRouter