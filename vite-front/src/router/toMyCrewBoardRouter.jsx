import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'

const Loading = <div className='loading'>Loading...</div>

const MyCrewBoardMain = lazy(() => import('../components/container/myCrew/board/MyCrewBoardContainer'))
const MyCrewBoardCreate = lazy(()=> import('../components/container/myCrew/board/MyCrewBoardCreateContainer'))
const MyCrewBoardDetail = lazy(() => import('../components/container/myCrew/board/MyCrewBoardDetailContainer'))
const MyCrewBoardUpdate = lazy(() => import('../components/container/myCrew/board/MyCrewBoardUpdateContainer'))

const toMyCrewBoardRouter = () => {
  return (
    [
        {
            // board/
            path:'',
            element: <Navigate replace to={'list'} />
        },
        {
            path: 'list',
            element: <Suspense fallback={Loading}><MyCrewBoardMain/></Suspense>
        },
        {
            path: 'create',
            element: <Suspense fallback={Loading}><MyCrewBoardCreate/></Suspense>
        },
        {
            path: 'detail/:boardId',
            element: <Suspense fallback={Loading}><MyCrewBoardDetail/></Suspense>
        },
        {
            path: 'update/:boardId',
            element: <Suspense fallback={Loading}><MyCrewBoardUpdate/></Suspense>
        }
    ]
  )
}

export default toMyCrewBoardRouter