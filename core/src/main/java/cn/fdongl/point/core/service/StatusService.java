package cn.fdongl.point.core.service;

import cn.fdongl.point.core.repository.StatuzRepository;
import cn.fdongl.point.model.entity.Statuz;
import org.hibernate.annotations.SQLInsert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StatusService {

    @Autowired
    StatuzRepository statuzRepository;

    private static Map<String,Integer> map = new Hashtable<>();

    public void updateStatus(String id,Integer value){
        Statuz statuz = new Statuz();
        statuz.setId(id);
        statuz.setVal(value);
        statuzRepository.save(statuz);
    }
    public Integer queryStatus(String id){
        Optional<Statuz> s = statuzRepository.findById(id);
        if(s.isPresent()){
            return s.get().getVal();
        }
        return null;
    }

}
