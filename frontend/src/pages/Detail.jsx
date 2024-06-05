import React, { useEffect } from 'react'
import {
  Container, Typography, Paper, Switch, Box,
  InputLabel, FormControl, Select, MenuItem
} from '@mui/material';
import './detail.scss';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import WarningIcon from '@mui/icons-material/Warning';
import LineChart from './Chart/LineChart';
import { useState } from 'react';
import Chart from 'chart.js/auto';
import { db } from '../utils/firebase'
import {Link, useParams, useSearchParams} from 'react-router-dom'
import { onValue, ref, set } from 'firebase/database';
import { getHealthIndex, getPantientDetail, saveHealthIndex } from '../utils/apis';
export default function Detail() {
  const [data, setData] = useState(null)
  const {id_machine} = useParams()
  const[mode, setMode] = useState(0)
  const [flow, setFlow] = useState(0)
  const [pantient, setPantient] = useState(null)
  const [beatHeart, setBeatHeart] = useState({
    labels: [],
    datasets: [{
      label: "Nhịp tim",
      data: []
    }]
  })
  const [spo2, setSpo2] = useState({
    labels: [],
    datasets: [{
      label: "SPO2",
      data: []
    }]
  })
  
  useEffect(() => {
    const myRef = ref(db, `may_tho/${id_machine}`)
    onValue(myRef, (snapshot) => {
      const newData = snapshot.val()
      setMode(newData.che_do)
      setFlow(newData.luu_luong)
      setData(newData)
      saveHealthIndex()
      if(!pantient) {
        newData && getPantientDetail(newData.id_benh_nhan).then((res) => {
          setPantient(res.data)
        })
      }
    })
    
  }, [])

  useEffect(() => {
    data && getHealthIndex(data.id_benh_nhan).then((res) => {
      const index_list = res.data
      const heartBeartList = []
      const spo2 = []
      index_list && index_list.forEach((element) => {
        heartBeartList.push(element.nhipTim)
        spo2.push(element.spo2)
      })
      setBeatHeart({
      labels: Array.from({ length: heartBeartList.length >= 15 ? 15 : heartBeartList.length}, (_, index) => index),
      datasets: [{
        label: "Nhịp tim",
        data: heartBeartList.slice(heartBeartList.length >= 15 ? -15 : -heartBeartList.length)
            }
          ]
        }
      )
        setSpo2({
        labels: Array.from({ length: spo2.length >= 15 ? 15 : spo2.length }, (_, index) => index),
        datasets: [{
          label: "SPO2",
          data: spo2.slice(spo2.length >= 15 ? -15 : -spo2.length - 1)
              }
            ]
          }
        )
    }, [])
  }, [data])
    
    

  const onPowerChange = () => {
    set(ref(db, `may_tho/${data.id}`),
      {
        ...data, 
        trang_thai: data.trang_thai === -1 ? -1 : (data.trang_thai === 1 ? 0 : 1),
        cap_nhap: 0
      });
  }
  const onFlowChange = (event) => {
    set(ref(db, `may_tho/${data.id}`),
      {
        ...data, 
        luu_luong: event.target.value,
        cap_nhap: 0
      });
  }
  const onModeChange = (event) => {
    set(ref(db, `may_tho/${data.id}`),
      {
        ...data, 
        che_do: event.target.value,
        cap_nhap: 0
      });
  }

  return (
    <Container maxWidth="lg" className="info-container">
      <Link to={'/'}>
        <ArrowBackIcon />
      </Link>
      <Typography variant="h4" gutterBottom>
        Chi tiết bệnh nhân
      </Typography>
      {data && data.id_benh_nhan === 0 ? (<Typography variant="h3" gutterBottom className='emtpy-text'>Máy chưa được ghép</Typography>)  : <Box>
        <Typography variant="h3" gutterBottom className='patient-infor'>
          <Box>
            Họ tên: {pantient && pantient.ten}
          </Box>
          <Box>
            Ngày sinh: {pantient && pantient.ngaySinh}
          </Box>
        </Typography>
      </Box>}
      <Paper className="info-paper">
        <Typography variant="body1" style={{ padding: "10px 0" }}>
          Bật/Tắt
          <Switch
            checked={data && data.trang_thai === 1}
            onChange={onPowerChange}
          />
        </Typography>
        <Box display="flex" justifyContent="space-between" style={{ padding: "10px 0" }}>
          <FormControl className='formSelect'>
            <InputLabel id="demo-simple-select-label">Lưu lượng</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              label="Lưu lượng"
              onChange={onFlowChange}
              value={flow}
            >
              <MenuItem value={0}>4 lít/phút</MenuItem>
              <MenuItem value={1}>6 lít/phút</MenuItem>
              <MenuItem value={2}>8 lít/phút</MenuItem>
            </Select>
          </FormControl>
          <FormControl className='formSelect'>
            <InputLabel id="demo-simple-select-label">Chế độ</InputLabel>
            <Select
              labelId="demo-simple-select-label"
              id="demo-simple-select"
              label="Chế độ"
              onChange={onModeChange}
              value={mode}
            >
              <MenuItem value={1}>Tự động</MenuItem>
              <MenuItem value={0}>Tuỳ chỉnh</MenuItem>
            </Select>
          </FormControl>
        </Box>
        <Typography variant="body1" style={{ padding: "10px 0" }}>
          Dung lượng oxy trong bình: {data && data.dung_luong} % {data && data.dung_luong <= 20 && <Typography className='warring-text'>(Dung lượng oxy trong bình sắp hết)</Typography>}
        </Typography>
        <Typography variant="body1" style={{ padding: "10px 0" }}>
          Bệnh lý: {pantient && pantient.benhLy}
        </Typography>
        <div className='chartContainer'>
          <div className="lineChart" >
            <LineChart chartData={beatHeart} lineColor="red" ></LineChart>
            <h3>
              Biểu đồ nhịp tim
              {data && data.nhip_tim < 50 && <Typography className='warring-text'>(Nhịp tim của bệnh nhân đang giảm)</Typography>}
              {data && data.nhip_tim > 120 && <Typography className='warring-text'>(Nhịp tim của bệnh nhân đang tăng)</Typography>}
            </h3>
          </div>
          <div className="lineChart" >
            <LineChart chartData={spo2} lineColor="purple" ></LineChart>
            <h3>
              Biểu đồ SPO2
              {data && data.spo2 <= 93 && <Typography className='warring-text'>(Nhịp tim của bệnh nhân đang giảm)</Typography>}
            </h3>
          </div>
        </div>
      </Paper>
    </Container>
  )
}
