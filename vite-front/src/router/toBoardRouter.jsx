import React, { lazy, Suspense } from 'react'
import { Navigate } from 'react-router-dom'


const Loading = <div className='loading'>Loading...</div>

const BoardList = lazy(()=> import(`../components/container/board/BoardListContainer`))
const BoardCalen = lazy(()=> import(`../components/container/board/BoradCalContainer`))
const BoardWrite = lazy(()=> import(`../components/container/board/BoardWriteContainer`))
const BoardDetail = lazy(()=> import(`../components/container/board/BoardDetailContainer`))
const BoardUpdate = lazy(()=> import(`../components/container/board/BoardUpdateContainer`))
const toBoardRouter = () => {
  return (
   [
    {

        path:'',
        element: <Navigate replace to={'index'} />
    },
    {

        path:'index',
        element: <Suspense fallback={Loading}><BoardList/></Suspense>
    },
    {

        path:'newPost',
        element: <Suspense fallback={Loading}><BoardWrite/></Suspense>
    },
    {

        path:'detail/:id',
        element: <Suspense fallback={Loading}><BoardDetail/></Suspense>

    },
    {
        path:'update/:id',
        element: <Suspense fallback={Loading}><BoardUpdate/></Suspense>
    },
    {
        path:'calender',
        element: <Suspense fallback={Loading}><BoardCalen/></Suspense>
    },
   
   ]
  )
}

export default toBoardRouter