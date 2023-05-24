// @mui
import { Box, Typography, useTheme, Link } from '@mui/material';
//assets
import moweLogo from '../../../../public/assets/icons/ic_mow_e.png';
//react-router
import { Link as RouterLink } from 'react-router-dom';

// ----------------------------------------------------------------------

export default function Logo() {
  const theme = useTheme();

  return (
    <Box sx={{ display: 'flex', justifyContent: 'center', alignItems: 'center', gap: 2 }} width={'100%'}>
      <Link to="/" component={RouterLink} sx={{ display: 'contents' }}>
        <Box
          component="img"
          sx={{
            height: 'auto',
            width: 80,
          }}
          alt="Logo"
          src={moweLogo}
        />
        <Typography variant="h4" color={theme.palette.text.primary}>
          Mow-e
        </Typography>
      </Link>
    </Box>
  );
}
