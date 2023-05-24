import { Helmet } from 'react-helmet-async';
// @mui
import { styled } from '@mui/material/styles';
import { Box, Container, Link, Typography } from '@mui/material';
// sections
import { LoginForm } from '../sections/auth/login';
// react-router
import { Link as RouterLink } from 'react-router-dom';

// ----------------------------------------------------------------------

const StyledRoot = styled('div')(({ theme }) => ({
  [theme.breakpoints.up('md')]: {
    display: 'flex',
  },
}));

const StyledContent = styled('div')(({ theme }) => ({
  maxWidth: 480,
  margin: 'auto',
  minHeight: '100vh',
  display: 'flex',
  justifyContent: 'center',
  flexDirection: 'column',
  padding: theme.spacing(12, 0),
}));

// ----------------------------------------------------------------------

export default function SignupPage() {
  return (
    <>
      <Helmet>
        <title> Sign Up | Mow-e </title>
      </Helmet>

      <StyledRoot>
        <Container maxWidth="sm">
          <StyledContent>
            <Box sx={{display: 'flex', justifyContent: 'center', mb: 5}}>
              <Typography variant="h4">Sign up to Mow-e Dashboard</Typography>
            </Box>

            <Typography variant="body2" sx={{ mb: 5 }}>
              Already have an account ? {''}
              <Link variant="subtitle2" to="/login" component={RouterLink}>
                Login
              </Link>
            </Typography>

            <LoginForm type={'Signup'}/>
          </StyledContent>
        </Container>
      </StyledRoot>
    </>
  );
}
