import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'
import toMyCrewBoardRouter from './toMyCrewBoardRouter'


const Loading = <div className='loading'>Loading...</div>

const MyCrewBoardLayout = lazy(() => import('../layout/MyCrewBoardLayout'))

const MyCrewMain = lazy(()=> import('../components/container/myCrew/MyCrewMainContainer'))
const MyCrewjoinRequest = lazy(()=> import('../components/container/myCrew/MyCrewJoinRequestContainer'))
const MyCrewMember = lazy(()=> import('../components/container/myCrew/MyCrewMemberContainer'))
const MyCrewRun = lazy(()=> import('../components/container/myCrew/MyCrewRunContainer'))
const MyCrewChat = lazy(()=> import('../components/container/myCrew/MyCrewChatContainer'))
const MyCrewUpdate = lazy(()=> import('../components/container/myCrew/MyCrewUpdateContainer'))

const toMyCrewRouter = () => {
  return (
    [
        {
            // mycrew/
            path:'',
            element: <Navigate replace to={'index'} />
        },
        {
            path: 'index',
            element: <Suspense fallback={Loading}><MyCrewMain/></Suspense>
        },
        {
          path: 'join',
          element: <Suspense fallback={Loading}><MyCrewjoinRequest/></Suspense>
        },
        {
            path: 'member',
            element: <Suspense fallback={Loading}><MyCrewMember/></Suspense>
        },
        {
            path: 'run',
            element: <Suspense fallback={Loading}><MyCrewRun/></Suspense>
        },
        {
            path: 'board',
            element: <Suspense fallback={Loading}><MyCrewBoardLayout/></Suspense>,
            children: toMyCrewBoardRouter(),
        },
        {
            path: 'chat',
            element: <Suspense fallback={Loading}><MyCrewChat/></Suspense>
        },
        {
            path: 'update',
            element: <Suspense fallback={Loading}><MyCrewUpdate/></Suspense>
        }
    ]
  )
}

export default toMyCrewRouter