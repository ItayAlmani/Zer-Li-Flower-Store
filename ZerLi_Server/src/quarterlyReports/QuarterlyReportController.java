package quarterlyReports;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import common.EchoServer;
import orderNproducts.entities.Store;
import quarterlyReports.interfaces.IQuarterlyReportController;

public class QuarterlyReportController implements IQuarterlyReportController {
	private static Timer timer;
	private static LocalDate nextDate;

	// Production
	/* (non-Javadoc)
	 * @see lior.IQuarterlyReportController#setAutoProductionTimer()
	 */
	@Override
	public void setAutoProductionTimer() {
		timer = new Timer(true);

		TimerTask t = new TimerTask() {

			@Override
			public void run() {
				try {
					if (LocalDate.now().equals(nextDate)) {
						ArrayList<Object> strObj = EchoServer.fac.store.getAllStores();
						for (Object obj : strObj) {
							EchoServer.fac.ordReport.produceOrderReport(LocalDate.now(), ((Store) obj).getStoreID());
							EchoServer.fac.incReport.produceIncomesReport(LocalDate.now(), ((Store) obj).getStoreID());
							EchoServer.fac.hisReport.produceHistogramOfCustomerComplaintsReport(LocalDate.now(),
									((Store) obj));
							EchoServer.fac.satReport.produceSatisfactionReport(LocalDate.now(),
									((Store) obj).getStoreID());
						}
						nextDate=nextDate.plusMonths(3);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Date date = null;
		int year = LocalDate.now().getYear();
		LocalDate now = LocalDate.now(), fstQ = LocalDate.of(year, 1, 1), sndQ = LocalDate.of(year, 4, 1),
				trdQ = LocalDate.of(year, 7, 1), frtQ = LocalDate.of(year, 10, 1);
		if (now.isAfter(fstQ.minusDays(1)) && now.isBefore(sndQ))
			date = Date.from(sndQ.atStartOfDay(ZoneId.systemDefault()).toInstant());
		else if (now.isAfter(sndQ.minusDays(1)) && now.isBefore(trdQ))
			date = Date.from(trdQ.atStartOfDay(ZoneId.systemDefault()).toInstant());
		else if (now.isAfter(trdQ.minusDays(1)) && now.isBefore(frtQ))
			date = Date.from(frtQ.atStartOfDay(ZoneId.systemDefault()).toInstant());
		else
			date = Date.from(fstQ.plusYears(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

		LocalDate chosenDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		nextDate = chosenDate.plusMonths(3);

		timer.schedule(t, date, 1000 * 60 * 60 * 24);

	}
}
