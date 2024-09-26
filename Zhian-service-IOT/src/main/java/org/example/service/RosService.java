package org.example.service;


import org.example.entity.InspectPoint;
import org.example.entity.Region;
import org.example.resp.ResponseMessage;
import org.springframework.web.bind.annotation.RequestBody;

public interface RosService {

    public ResponseMessage updateRegion(@RequestBody Region region);

    public ResponseMessage getRegion(String workshop);

    public ResponseMessage getInspectPoints(String workshop);

    public ResponseMessage updateInspectPoints(@RequestBody InspectPoint[] inspectPoints);
}
