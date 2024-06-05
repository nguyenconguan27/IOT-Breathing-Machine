import axios from "axios";
import { baseURL } from "./baseURL";
import { onValue, ref, set } from "firebase/database";
import { db } from "./firebase";

export const saveHealthIndex = () => {
  const newIndex = [];
  const myRef = ref(db, "may_tho");
  let newData;
  onValue(myRef, (snapshot) => {
    newData = snapshot.val();
  });
  newData &&
    newData.forEach((element) => {
      if (element.trang_thai !== -1 && element.cap_nhap === 1) {
        newIndex.push({
          idBenhNhan: element.id_benh_nhan,
          nhipTim: element.nhip_tim,
          spo2: element.spo2,
        });
        set(ref(db, `may_tho/${element.id}`), {
          ...element,
          cap_nhap: 2,
        });
      }
    });
  axios({
    method: "post",
    url: baseURL + "/chiSo/save/list",
    data: newIndex,
  }).then((response) => {});
  return newData;
};

export const getHealthIndex = (pantient_id) => {
  return axios.get(baseURL + `/chiSo/benhNhan`, {
    params: { idBenhNhan: pantient_id },
  });
};

export const getPantientDetail = (pantient_id) => {
  return axios.get(baseURL + `/benhNhan`, {
    params: {
      idBenhNhan: pantient_id,
    },
  });
};

// export const addPantient = (pantient) => () => {
//   return axios({
//     method: "post",
//     url: baseURL + "/benhNhan/import",
//     data: pantient,
//   });
// };

// export const addPantientAndMachine = (id_machine, pantient) => () => {
//   return axios({
//     url: baseURL + "/mayTho/ketNoi",
//     params: { idMayTho: id_machine },
//     data: pantient,
//   });
// };

export const deletePantient = (id_pantient, id_machine) => {
  axios({
    method: "delete",
    url: baseURL + "/benhNhan/delete",
    params: {
      id: id_pantient,
      idMayTho: id_machine,
    },
  })
    .then((res) => {
      console.log(res);
    })
    .catch((error) => {
      console.log(error);
    });
};
