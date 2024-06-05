import React, {useState} from 'react'
import { Paper, TextField, Button, Typography, Container } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import './addPantient.scss';
import {Link, useParams} from 'react-router-dom'
import { addPantient, addPantientAndMachine } from '../utils/apis';
import { baseURL } from '../utils/baseURL';
import axios from 'axios';
import { onValue, ref, set } from 'firebase/database';
import { db } from '../utils/firebase';
import { toast } from 'react-toastify';
export default function AddPantient() {
  const {id_machine} = useParams()
  const [user, setUser] = useState(null)
  const onChange = (event) => {
    setUser({...user, [event.target.name]: event.target.value})
  }
  const  addPantient = () => {
    if(user && user.ten && user.ngaySinh && user.diaChi && user.benhLy) {
      axios({
      method: "post",
      url: baseURL + "/benhNhan/import",
      data: user,
      })
      .then((res) => {
        const pantient = res.data
        axios({
          method: "put",
          url: baseURL + "/mayTho/ketNoi",
          params: { idMayTho: id_machine },
          data: {...user, id: pantient.id},
        })
        .then(res => {
          const myRef = ref(db, `may_tho/${id_machine}`);
          let machine;
          onValue(myRef, (snapshot) => {
            machine = snapshot.val();
          });
          set(ref(db, `may_tho/${id_machine}`), {
            ...machine,
            cap_nhap: 0,
            trang_thai: 0,
            id_benh_nhan: pantient.id
          });
          toast.success("Thêm bênh nhân thành công !", {
            autoClose: 1000
          });
        })
      })
      .catch(error => {
        toast.success('Thêm bệnh nhân thất bại', {
          autoClose: 1000
        })
      });
    }
    else {
      toast.warning("vui lòng điền đầy đủ thông tin", {
        autoClose: 2000
      })
    }
    
  }
  return (
  
      <Container  className='formContainer'>
        <Paper className="info-form">
          <Link to={'/'}>
            <ArrowBackIcon />
          </Link>
          <Typography variant="h5" gutterBottom style={{textAlign:"center"}}>
            Thêm Thông Tin Bệnh Nhân
          </Typography>
          <div className="form-container">
            <TextField required label="Tên" variant="outlined" fullWidth margin="normal" name="ten" onChange={onChange}/>
            <TextField required label="Ngày Sinh" variant="outlined" fullWidth margin="normal" name="ngaySinh" onChange={onChange}/>
            <TextField required label="Địa Chỉ" variant="outlined" fullWidth margin="normal" name="diaChi" onChange={onChange}/>
            <TextField required label="Bệnh Lý" variant="outlined" fullWidth margin="normal" name= "benhLy" onChange={onChange}/>
            <Button variant="contained" color="primary" fullWidth onClick={addPantient}>
              Thêm
            </Button>
          </div>
        </Paper>
      </Container>
  )
}
