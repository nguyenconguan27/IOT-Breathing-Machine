import { Box, List, ListItem, ListItemAvatar, ListItemText, Avatar, IconButton } from '@mui/material';
import PropaneTankSharpIcon from '@mui/icons-material/PropaneTankSharp';
import PersonAddAltSharpIcon from '@mui/icons-material/PersonAddAltSharp';
import PersonRemoveSharpIcon from '@mui/icons-material/PersonRemoveSharp';
import WarningIcon from '@mui/icons-material/Warning';
import React, { useEffect, useState } from 'react'
import { ref, onValue, set } from "firebase/database"
import { db } from '../utils/firebase'
import './home.scss'
import { Link } from 'react-router-dom';
import { saveHealthIndex } from '../utils/apis';
import axios from 'axios';
import { baseURL } from '../utils/baseURL';
import { toast } from 'react-toastify';

export default function InteractiveList() {
  const [data, setData] = useState(null)
  useEffect(() => {
    const myRef = ref(db, "may_tho")
    onValue(myRef, (snapshot) => {
      const newData = snapshot.val()
      setData(newData)
      saveHealthIndex()
    })
  }, [])

  const deletePantient = (machine) => () => {
    // call api xoa du lieu
    axios({
      method: "delete",
      url: baseURL + "/benhNhan/delete",
      params: {
        id: machine.id_benh_nhan,
        idMayTho: machine.id,
      },
    })
      .then((res) => {
        console.log(res)
        set(ref(db, `may_tho/${machine.id}`),{
          ...machine, trang_thai: -1, id_benh_nhan : 0, cap_nhap : 0
        });
        toast.success("Xoá bệnh nhân thành công !")
      })
      .catch((error) => {
        toast.error("Xoá bệnh nhân thất bại")
      });
    //set trang thai
    
    
  }
  return (
    <Box sx={{ flexGrow: 1 }}>
      <div className='machine-container'>
        <h1 style={{textAlign:"center"}}>Danh sách máy thở</h1>
        <List className='machine-list'>
          {data && data.map((machine) => (
                 <ListItem
                  className='machine-item'
                  key={machine.id}
                  secondaryAction={
                   <IconButton>
                     {machine.trang_thai === -1 ? (
                       <Link to={`/add/${machine.id}`}>
                         <PersonAddAltSharpIcon className='add-pantient-btn'/>
                       </Link>) : 
                       (<PersonRemoveSharpIcon className='delete-pantient-btn' onClick={deletePantient(machine)}/>)}
                   </IconButton>
                 }
               >
                 <Link to={`/detail/${machine.id}`} className='machine-link'>
                   <ListItemAvatar className='avatar' >
                     <Avatar className='avatarMachine'>
                       <PropaneTankSharpIcon />
                     </Avatar>
                     {(machine.nhip_tim < 50 || machine.nhip_tim > 120 || machine.spo2 < 93 || machine.dung_luong < 25) && machine.trang_thai != -1 && <WarningIcon className='warning' />}
                     
                   </ListItemAvatar>
                   <ListItemText className='avatarText'
                     primary={<strong>Máy số {machine.id}</strong>}
                     secondary={<div>Trạng thái : {machine.trang_thai === -1 ? "Trống" : "Đã ghép"}</div>}
                   />
                 </Link>
               </ListItem>
              ))}
        </List>
      </div>
    </Box>

  );
}
