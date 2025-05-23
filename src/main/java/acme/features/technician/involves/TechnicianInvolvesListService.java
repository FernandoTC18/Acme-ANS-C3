
package acme.features.technician.involves;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.involves.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.features.technician.maintenanceRecord.TechnicianMaintenanceRecordRepository;
import acme.realms.Technician;

@GuiService
public class TechnicianInvolvesListService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	TechnicianInvolvesRepository			repository;

	@Autowired
	TechnicianMaintenanceRecordRepository	mRRepository;


	@Override
	public void authorise() {
		Boolean status = false;
		int maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		MaintenanceRecord mR = this.mRRepository.findMaintenanceRecordById(maintenanceRecordId);

		if (mR != null && super.getRequest().getPrincipal().hasRealm(mR.getTechnician()))
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Involves> involves;

		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);
		involves = this.repository.findInvolvesByMaintenanceRecordId(maintenanceRecordId);

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);

		super.getBuffer().addData(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;

		int maintenanceRecordId;

		maintenanceRecordId = super.getRequest().getData("maintenanceRecordId", int.class);

		dataset = super.unbindObject(involves);

		dataset.put("aircraft", involves.getMaintenanceRecord().getAircraft());
		dataset.put("task", involves.getTask().getDescription());
		dataset.put("priority", involves.getTask().getPriority());
		dataset.put("estimatedDuration", involves.getTask().getEstimatedDuration());
		dataset.put("technician", involves.getTask().getTechnician());

		super.getResponse().addGlobal("maintenanceRecordId", maintenanceRecordId);

		super.getResponse().addData(dataset);
	}

}
