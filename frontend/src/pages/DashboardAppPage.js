import { Helmet } from 'react-helmet-async';
import { Card, Grid, Container, Typography, useTheme, CardHeader, CardContent, Stack } from '@mui/material';
// sections
import { AppWidgetSummary } from '../sections/@dashboard/app';
//react
import { useEffect, useState } from 'react';
import api from '../utils/axios';
// components
import ActionItem from '../components/actionItem';
// ----------------------------------------------------------------------

export default function DashboardAppPage() {
  const [countOwners, setCountOwners] = useState(0);
  const [totalHistorySessions, setTotalHistorySessions] = useState(0);
  const [countOwned, setCountOwned] = useState(0);
  const [countActive, setCountActive] = useState(0);
  const [imageSize, setImageSize] = useState(0);

  useEffect(() => {
    const fetchCountOwners = async () => {
      try {
        api
          .get('api/dashboard/users/countOwners')
          .then((response) => {
            setCountOwners(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
      } catch (error) {
        console.error(error);
      }
    };

    const fetchTotalHistorySessions = async () => {
      try {
        api
          .get('api/dashboard/mowers/totalHistorySessions')
          .then((response) => {
            setTotalHistorySessions(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
      } catch (error) {
        console.error(error);
      }
    };

    const fetchCountOwned = async () => {
      try {
        api
          .get('api/dashboard/mowers/countOwned')
          .then((response) => {
            setCountOwned(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
      } catch (error) {
        console.error(error);
      }
    };

    const fetchCountActive = async () => {
      try {
        api
          .get('api/dashboard/mowers/countActive')
          .then((response) => {
            setCountActive(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
      } catch (error) {
        console.error(error);
      }
    };

    const fetchImageSize = async () => {
      try {
        api
          .get('api/dashboard/imageStorage/size')
          .then((response) => {
            setImageSize(response.data);
          })
          .catch((error) => {
            console.error(error);
          });
      } catch (error) {
        console.error(error);
      }
    };

    fetchCountOwners();
    fetchTotalHistorySessions();
    fetchCountOwned();
    fetchCountActive();
    fetchImageSize();
  }, []);

  return (
    <>
      <Helmet>
        <title> Dashboard | Mow-e </title>
      </Helmet>

      <Container maxWidth="xl">
        <Typography variant="h4" sx={{ mb: 5 }}>
          Hi, Welcome back
        </Typography>

        <Grid container spacing={3}>
          <Grid item xs={12} sm={6} md={4}>
            <AppWidgetSummary title="Count Owners" total={countOwners} />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <AppWidgetSummary title="Total History Sessions" total={totalHistorySessions} color="info" />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <AppWidgetSummary title="Count Owned" total={countOwned} color="warning" />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <AppWidgetSummary title="Count Active" total={countActive} color="error" />
          </Grid>

          <Grid item xs={12} sm={6} md={4}>
            <AppWidgetSummary title="Image Storage Size" total={imageSize} color="error" />
          </Grid>

          <Grid item xs={12} md={6} lg={8}>
            <Card>
              <CardHeader title="Actions">Actions</CardHeader>
              <CardContent>
                <Stack>
                  <ActionItem
                    title="LobBook"
                    buttonText="Open"
                    onClick={() => window.open('http://mow-e.me/actuator/logfile', '_blank')}
                  />
                  <ActionItem
                    title="Image Storage"
                    buttonText="Clean"
                    onClick={() => {
                      api
                        .post('dashboard/imageStorage/clean')
                        .then((response) => {
                          console.log(response);
                        })
                        .catch((error) => {
                          console.error(error);
                        });
                    }}
                  />
                  <ActionItem
                    title="Image History"
                    buttonText="Open"
                    onClick={() => {
                      api
                        .get('api/mower/images/54ea1569-47e9-4720-992d-dc0b69a3c5ed')
                        .then((response) => {
                          console.log(response);
                        })
                        .catch((error) => {
                          console.error(error);
                        });
                    }}
                  />
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </>
  );
}
