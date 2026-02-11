import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'


const Loading = <div className='loading'>Loading...</div>


const CrewMain = lazy(()=> import('../components/container/crew/CrewMainContainer'))
const CrewDetail = lazy(() => import('../components/container/crew/CrewDetailContainer'))
const CrewCreateRequest = lazy(() => import('../components/container/crew/CrewCreateRequestContainer'))

const toCrewRouter = () => {
  return (
    [
        {
            // crew/
            path:'',
            element: <Navigate replace to={'index'} />
        },
        {
            path: 'index',
            element: <Suspense fallback={Loading}><CrewMain/></Suspense>
        },
        {
            path: 'list',
            element: <Suspense fallback={Loading}><CrewMain/></Suspense>
        },
        {
            path: 'detail/:crewId',
            element: <Suspense fallback={Loading}><CrewDetail/></Suspense>
        },
        {
            path: 'createRequest',
            element: <Suspense fallback={Loading}><CrewCreateRequest/></Suspense>
        }
    ]
  )
}

export default toCrewRouter