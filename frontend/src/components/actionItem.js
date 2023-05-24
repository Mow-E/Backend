import { Box, Button, Typography } from '@mui/material'
import React from 'react'

export default function ActionItem({title, buttonText, onClick}) {
  return (
    <Box sx={{display: 'flex', justifyContent: 'space-between', height: '50px'}}>
      <Box>
        <Typography>{title}</Typography>
      </Box>
      <Box>
        <Button variant='contained' onClick={onClick}>{buttonText}</Button>
      </Box>
    </Box>
  )
}
