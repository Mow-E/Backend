import { Navigate, useRoutes } from 'react-router-dom';
// layouts
import DashboardLayout from './layouts/dashboard';
//
import UserPage from './pages/UserPage';
import Page404 from './pages/Page404';
import DashboardAppPage from './pages/DashboardAppPage';
import Login from './pages/Login';
import SignupPage from './pages/Signup';
// react
import { useContext } from 'react';
import MyContext from './context/myContext';

// ----------------------------------------------------------------------

export default function Router() {
  const {token, setToken} = useContext(MyContext);
  

  const dashboardAppPage = token ? (
    { path: 'app', element: <DashboardAppPage /> }
  ) : (
    { path: 'app', element: <Navigate to="/login" replace /> }
  );
  
  const routes = useRoutes([
    {
      path: '/dashboard',
      element: <DashboardLayout />,
      children: [
        { element: <Navigate to="/dashboard/app" />, index: true },
        dashboardAppPage,
        { path: 'user', element: <UserPage /> },
      ],
    },
    {
      children: [
        { element: <Navigate to="/dashboard/app" />, index: true },
        { path: '404', element: <Page404 /> },
        { path: '*', element: <Navigate to="/404" /> },
      ],
    },
    {
      path: '/login',
      element: <Login />,
    },
    {
      path: '/signup',
      element: <SignupPage />,
    },
    {
      path: '*',
      element: <Navigate to="/404" replace />,
    },
  ]);

  return routes;
}
