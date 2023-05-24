import { useState, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
// @mui
import { Link, Stack, IconButton, InputAdornment, TextField, Checkbox } from '@mui/material';
import { LoadingButton } from '@mui/lab';
// components
import Iconify from '../../../components/iconify';
// api
import api, { setApiToken } from '../../../utils/axios';
import MyContext from '../../../context/myContext';

// ----------------------------------------------------------------------

export default function LoginForm({type = 'Login'| 'Signup'}) {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const {token, setToken} = useContext(MyContext);

  const handleClick = () => {
    if (type === 'Login') {
      login();
    } else {
      signup();
    }
  };

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const login = async () => {
    try {
      const response = await api.post('auth/login', {
        username: username,
        password: password,
      });
      
      setToken(response.data.token)
      setApiToken(response.data.token);
      navigate('/dashboard', { replace: true });

    } catch (error) {
      console.log(error);
    }
  };

  const signup = async () => {
    try {
      const response = await api.get('auth/signup', {
        username: username,
        password: password,
      });
      const setToken = useContext(MyContext);
      setToken(response.data.token);
    } catch (error) {
      console.log(error);
    }
  };


  return (
    <>
      <Stack spacing={3} sx={{mb: 2}}>
        <TextField name="username" label="Username" value={username} onChange={(e) => setUsername
        (e.target.value)} />

        <TextField
          name="password"
          label="Password"
          type={showPassword ? 'text' : 'password'}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                  <Iconify icon={showPassword ? 'eva:eye-fill' : 'eva:eye-off-fill'} />
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
      </Stack>

      <LoadingButton fullWidth size="large" type="submit" variant="contained" onClick={handleClick}>
        {type}
      </LoadingButton>
    </>
  );
}
