import React from 'react';

import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import FData from './FData';
import Graphbase from './Graphbase';
import GraphMsbase from './GraphMsbase';
import { categoryColumns } from './tables/CategoryTable';
import { checksColumns } from './tables/ChecksTable';
import { checkTypeColumns } from './tables/CheckTypeTable';
import { csbTypeColumns } from './tables/CsbTypeTable';
import { dupsColumns } from './tables/DupsTable';
import { firstColumns } from './tables/FirstTable';
import { labelColumns } from './tables/LabelTable';
import { ledgerColumns } from './tables/LedgerTable';
import { locationColumns } from './tables/LocationTable';
import { ltypeColumns } from './tables/LtypeTable';
import { mltypeColumns } from './tables/MltypeTable';
import { namesColumns } from './tables/NamesTable';
import { payeeColumns } from './tables/PayeeTable';
import { statementColumns } from './tables/StatementTable';
import { statementsColumns } from './tables/StatementsTable';
import { stypemapColumns } from './tables/StypemapTable';
import { stypeColumns } from './tables/StypeTable';
import { utilitiesColumns } from './tables/UtilitiesTable';
import { budgetColumns } from './tables/BudgetTable';
import { budgetvaluesColumns } from './tables/BudgetValueTable';
import { budgetdatavaluesColumns } from './tables/BudgetDataValueTable';
import { transferColumns } from './tables/TransferTable';
import { inoutnetColumns } from './tables/InOutNetTable';
import { balanceColumns } from './tables/BalanceTable';
import { stypeDataColumns } from './tables/StypeDataTable';
import { billsColumns } from './tables/BillsTable';
import { annualColumns } from './tables/AnnualTable';
import { utilsColumns } from './tables/UtilsTable';
import { creditpaidColumns } from './tables/CreditPaidTable';
import { categoriesColumns } from './tables/CategoriesTable';
import { cstatusColumns } from './tables/CStatusTable';
import { tledgerColumns } from './tables/TledgerTable';
import { payperiodColumns } from './tables/PayperiodTable';
import { kvpColumns } from './tables/KvpTable';
import { wdatamapColumns } from './tables/WdatamapTable';
import { gscatColumns } from './tables/GscatTable';
import { lmapColumns } from './tables/LmapTable';
import { cmapColumns } from './tables/CmapTable';
import { csbtColumns } from './tables/CsbtTable';
import { mltColumns } from './tables/MltTable';

function Tv(props) {
  return (
    <TreeView defaultCollapseIcon={<ExpandMoreIcon />} defaultExpandIcon={<ChevronRightIcon />}  >
      <TreeItem nodeId="1" label="Tables">
        <TreeItem nodeId="2" label="Category">
          <FData session={props} columns={categoryColumns} title="Categories" urlt="categories" jsonp="categories"/>
        </TreeItem>
        <TreeItem nodeId="7" label="Checks">
          <FData session={props} columns={checksColumns} title="Checks" urlt="checks" jsonp="checks"/>
        </TreeItem>
        <TreeItem nodeId="8" label="Checktype">
          <FData session={props} columns={checkTypeColumns} title="Check Types" urlt="checktypes" jsonp="checkTypes"/>
        </TreeItem>
        <TreeItem nodeId="9" label="Csbtype">
          <FData session={props} columns={csbTypeColumns} title="CSB Types" urlt="CsbTypes" jsonp="csbTypes"/>
        </TreeItem>
        <TreeItem nodeId="10" label="Dups">
          <FData session={props} columns={dupsColumns} title="Dups" urlt="Dups" jsonp="dups"/>
        </TreeItem>
        <TreeItem nodeId="11" label="First">
          <FData session={props} columns={firstColumns} title="First" urlt="First" jsonp="first"/>
        </TreeItem>
        <TreeItem nodeId="12" label="Label">
          <FData session={props} columns={labelColumns} title="Label" urlt="Label" jsonp="label"/>
        </TreeItem>
        <TreeItem nodeId="13" label="Ledger">
          <FData session={props} columns={ledgerColumns} title="Ledger" urlt="ledgertable" jsonp="ledger"/>
        </TreeItem>
        <TreeItem nodeId="14" label="Location">
          <FData session={props} columns={locationColumns} title="Location" urlt="locationtable" jsonp="location"/>
        </TreeItem>
        <TreeItem nodeId="15" label="Ltype">
          <FData session={props} columns={ltypeColumns} title="Account" urlt="ltypetable" jsonp="ltype"/>
        </TreeItem>
        <TreeItem nodeId="16" label="Mltype">
          <FData session={props} columns={mltypeColumns} title="ML Type" urlt="Mltypes" jsonp="mltypes"/>
        </TreeItem>
        <TreeItem nodeId="17" label="Names">
          <FData session={props} columns={namesColumns} title="ML Type" urlt="Names" jsonp="names"/>
        </TreeItem>
        <TreeItem nodeId="18" label="Payee">
          <FData session={props} columns={payeeColumns} title="Payee" urlt="Payee" jsonp="payee"/>
        </TreeItem>
        <TreeItem nodeId="19" label="Statement">
          <FData session={props} columns={statementColumns} title="Statement" urlt="statementable" jsonp="statement"/>
        </TreeItem>
        <TreeItem nodeId="20" label="Statements">
          <FData session={props} columns={statementsColumns} title="Statements" urlt="Statements" jsonp="statements"/>
        </TreeItem>
        <TreeItem nodeId="21" label="Stype">
          <FData session={props} columns={stypeColumns} title="Stype" urlt="Stypes" jsonp="stype"/>
        </TreeItem>
        <TreeItem nodeId="22" label="Stypemap">
          <FData session={props} columns={stypemapColumns} title="Stypemap" urlt="stypemaptable" jsonp="stypemap"/>
        </TreeItem>
        <TreeItem nodeId="23" label="Transfer">
          <FData session={props} columns={transferColumns} title="Transfer" urlt="transfertable" jsonp="transfer"/>
        </TreeItem>
        <TreeItem nodeId="24" label="Utilities">
          <FData session={props} columns={utilitiesColumns} title="Utilities" urlt="Utilities" jsonp="utilities"/>
        </TreeItem>
        <TreeItem nodeId="25" label="Budget">
          <FData session={props} columns={budgetColumns} title="Budget" urlt="budgettable" jsonp="budget"/>
        </TreeItem>
        <TreeItem nodeId="26" label="BudgetValues">
          <FData session={props} columns={budgetvaluesColumns} title="Budgetvalues" urlt="budgetvaluetable" jsonp="budgetvalues"/>
        </TreeItem>
        <TreeItem nodeId="84" label="Budgets">
          <FData session={props} columns={budgetColumns} title="Budgets" urlt="budgetstable" jsonp="budgets"/>
        </TreeItem>
        <TreeItem nodeId="86" label="Tledger">
          <FData session={props} columns={tledgerColumns} title="Tledger" urlt="tledgertable" jsonp="tledger"/>
        </TreeItem>
        <TreeItem nodeId="87" label="Payperiod">
          <FData session={props} columns={payperiodColumns} title="Payperiod" urlt="Payperiod" jsonp="payperiod"/>
        </TreeItem>
        <TreeItem nodeId="88" label="Kvp">
          <FData session={props} columns={kvpColumns} title="Kvp" urlt="Kvp" jsonp="kvp"/>
        </TreeItem>
        <TreeItem nodeId="89" label="Wdatamap">
          <FData session={props} columns={wdatamapColumns} title="Wdatamap" urlt="wdatamaptable" jsonp="wdatamap"/>
        </TreeItem>
        <TreeItem nodeId="90" label="Gscat">
          <FData session={props} columns={gscatColumns} title="Gscat" urlt="Gscat" jsonp="gscat"/>
        </TreeItem>
        <TreeItem nodeId="91" label="Lmap">
          <FData session={props} columns={lmapColumns} title="Lmap" urlt="Lmap" jsonp="lmap"/>
        </TreeItem>
        <TreeItem nodeId="92" label="Cmap">
          <FData session={props} columns={cmapColumns} title="Cmap" urlt="Cmap" jsonp="cmap"/>
        </TreeItem>
        <TreeItem nodeId="93" label="Csbts">
          <FData session={props} columns={csbtColumns} title="Csbt" urlt="csbttable" jsonp="csbt"/>
        </TreeItem>
        <TreeItem nodeId="98" label="Mlts">
          <FData session={props} columns={mltColumns} title="Mlt" urlt="mlttable" jsonp="mlt"/>
        </TreeItem>
      </TreeItem>
      <TreeItem nodeId="3" label="Data">
        <TreeItem nodeId="4" label="Main">
          <FData usession={props} columns={ledgerColumns} title="Ledger" urlt="ledger" jsonp="ledger"/>
        </TreeItem>
        <TreeItem nodeId="5" label="InOutNet">
          <FData usession={props} columns={inoutnetColumns} title="In Out Net" urlt="inoutnet" jsonp="inoutnet"/>
        </TreeItem>
        <TreeItem nodeId="6" label="Balances">
          <FData usession={props} columns={balanceColumns} title="Balances" urlt="balance" jsonp="balance"/>
        </TreeItem>
        <TreeItem nodeId="75" label="Stypes">
          <FData usession={props} columns={stypeDataColumns} title="Stypes" urlt="stype" jsonp="stype"/>
        </TreeItem>
        <TreeItem nodeId="76" label="Bills">
          <FData usession={props} columns={billsColumns} title="Bills" urlt="bills" jsonp="bills"/>
        </TreeItem>
        <TreeItem nodeId="77" label="Statements">
          <FData usession={props} columns={statementColumns} title="Statements" urlt="statements" jsonp="statement"/>
        </TreeItem>
        <TreeItem nodeId="78" label="Annual">
          <FData usession={props} columns={annualColumns} title="Annual" urlt="annual" jsonp="annual"/>
        </TreeItem>
        <TreeItem nodeId="79" label="Utils">
          <FData usession={props} columns={utilsColumns} title="Utils" urlt="utils" jsonp="utils"/>
        </TreeItem>
        <TreeItem nodeId="80" label="CreditPaid">
          <FData usession={props} columns={creditpaidColumns} title="CreditPaid" urlt="creditp" jsonp="credit"/>
        </TreeItem>
        <TreeItem nodeId="81" label="CreditSpent">
          <FData usession={props} columns={creditpaidColumns} title="CreditSpent" urlt="credits" jsonp="credit"/>
        </TreeItem>
        <TreeItem nodeId="31" label="Categories">
          <FData usession={props} columns={categoriesColumns} title="Categories" urlt="categories" jsonp="categories"/>
        </TreeItem>
        <TreeItem nodeId="74" label="CStatus">
          <FData usession={props} columns={cstatusColumns} title="CStatus" urlt="cstatus" jsonp="cstatus"/>
        </TreeItem>

        <TreeItem nodeId="71" label="Budget">
          <TreeItem nodeId="72" label="Budget Values">
            <FData usession={props} columns={budgetdatavaluesColumns} title="Values" urlt="bvalues" jsonp="bvn"/>
          </TreeItem>
          <TreeItem nodeId="97" label="Budgets Values">
            <FData usession={props} columns={budgetdatavaluesColumns} title="SValues" urlt="bvs" jsonp="bvn"/>
          </TreeItem>
          <TreeItem nodeId="73" label="Budget Net">
            <FData usession={props} columns={budgetdatavaluesColumns} title="Net" urlt="bnet" jsonp="bvn"/>
          </TreeItem>
          <TreeItem nodeId="85" label="Budgets Net">
            <FData usession={props} columns={budgetdatavaluesColumns} title="SNet" urlt="bsnet" jsonp="bvn"/>
          </TreeItem>
       </TreeItem>


      </TreeItem>
      <TreeItem nodeId="95" label="Graphs">
        <TreeItem nodeId="96" label="Msline">
          <GraphMsbase usession={props} urlt="msline"/>
        </TreeItem>
        <TreeItem nodeId="41" label="SType">
          <TreeItem nodeId="82" label="stypeAtm">
            <Graphbase usession={props} urlt="stype/atm"/>
          </TreeItem>
          <TreeItem nodeId="83" label="stypeBills">
            <Graphbase usession={props} urlt="stype/bills"/>
          </TreeItem>
          <TreeItem nodeId="27" label="stypeMisc">
            <Graphbase usession={props} urlt="stype/misc"/>
          </TreeItem>
          <TreeItem nodeId="28" label="stypePos">
            <Graphbase usession={props} urlt="stype/pos"/>
          </TreeItem>
          <TreeItem nodeId="29" label="stypeAnnual">
            <Graphbase usession={props} urlt="stype/annual"/>
          </TreeItem>
          <TreeItem nodeId="30" label="stypeCredit">
            <Graphbase usession={props} urlt="stype/credit"/>
          </TreeItem>
        </TreeItem>
        <TreeItem nodeId="32" label="Category Credit">
           <Graphbase usession={props} urlt="category/credit"/>
        </TreeItem>
        <TreeItem nodeId="33" label="Category Debit">
           <Graphbase usession={props} urlt="category/debit"/>
        </TreeItem>
        <TreeItem nodeId="94" label="CSB Balance">
          <Graphbase usession={props} urlt="csbt"/>
        </TreeItem>
        <TreeItem nodeId="99" label="ML Balance">
          <Graphbase usession={props} urlt="mlt"/>
        </TreeItem>
        <TreeItem nodeId="34" label="electric">
           <Graphbase usession={props} urlt="electric"/>
        </TreeItem>
        <TreeItem nodeId="64" label="cell">
           <Graphbase usession={props} urlt="cell"/>
        </TreeItem>
        <TreeItem nodeId="65" label="cable">
           <Graphbase usession={props} urlt="cable"/>
        </TreeItem>
        <TreeItem nodeId="35" label="out">
           <Graphbase usession={props} urlt="out"/>
         </TreeItem>
         <TreeItem nodeId="49" label="in">
           <Graphbase usession={props} urlt="in"/>
         </TreeItem>
         <TreeItem nodeId="36" label="utilities">
            <Graphbase usession={props} urlt="utilities"/>
         </TreeItem>
         <TreeItem nodeId="37" label="POS">
            <Graphbase usession={props} urlt="pos"/>
         </TreeItem>
         <TreeItem nodeId="38" label="ATM">
            <Graphbase usession={props} urlt="atm"/>
         </TreeItem>
         <TreeItem nodeId="70" label="Misc">
           <Graphbase usession={props} urlt="misc"/>
          </TreeItem>
         <TreeItem nodeId="39" label="Dog">
           <Graphbase usession={props} urlt="dog"/>
         </TreeItem>
         <TreeItem nodeId="40" label="ML">
            <Graphbase usession={props} urlt="ml"/>
         </TreeItem>
          <TreeItem nodeId="67" label="Credit Off">
            <TreeItem nodeId="68" label="Capital One Off">
              <Graphbase usession={props} urlt="caponeoff"/>
            </TreeItem>
            <TreeItem nodeId="69" label="Aaa Off">
              <Graphbase usession={props} urlt="aaaoff"/>
            </TreeItem>
          </TreeItem>
         <TreeItem nodeId="42" label="Credit">
           <TreeItem nodeId="43" label="Capital One">
             <Graphbase usession={props} urlt="capone"/>
           </TreeItem>
           <TreeItem nodeId="66" label="Capital One Filtered">
             <Graphbase usession={props} urlt="caponefilter"/>
           </TreeItem>
           <TreeItem nodeId="44" label="Amazon">
             <Graphbase usession={props} urlt="amazon"/>
           </TreeItem>
           <TreeItem nodeId="45" label="Usaa">
             <Graphbase usession={props} urlt="usaa"/>
           </TreeItem>
           <TreeItem nodeId="46" label="AAA">
             <Graphbase usession={props} urlt="aaa"/>
           </TreeItem>
           <TreeItem nodeId="47" label="All">
             <Graphbase usession={props} urlt="credit"/>
           </TreeItem>
         </TreeItem>
         <TreeItem nodeId="48" label="Budget">
            <TreeItem nodeId="53" label="Credit">
                <TreeItem nodeId="54" label="Capital One">
                    <Graphbase usession={props} urlt="bcapone"/>
                </TreeItem>
                <TreeItem nodeId="55" label="Amazon">
                    <Graphbase usession={props} urlt="bamazon"/>
                </TreeItem>
                <TreeItem nodeId="56" label="Usaa">
                    <Graphbase usession={props} urlt="busaa"/>
                </TreeItem>
                <TreeItem nodeId="57" label="AAA">
                     <Graphbase usession={props} urlt="baaa"/>
                </TreeItem>
                <TreeItem nodeId="58" label="Sears">
                      <Graphbase usession={props} urlt="bsears"/>
                </TreeItem>
                <TreeItem nodeId="59" label="All">
                     <Graphbase usession={props} urlt="bcredit"/>
                </TreeItem>
            </TreeItem>
            <TreeItem nodeId="60" label="Utilities">
                <Graphbase usession={props} urlt="butilities"/>
            </TreeItem>
            <TreeItem nodeId="61" label="Medical">
                <Graphbase usession={props} urlt="bmedical"/>
            </TreeItem>
            <TreeItem nodeId="62" label="Mortgage">
                <Graphbase usession={props} urlt="bmortgage"/>
            </TreeItem>
            <TreeItem nodeId="63" label="consolidated budget">
                <Graphbase usession={props} urlt="budget"/>
            </TreeItem>
         </TreeItem>
         <TreeItem nodeId="50" label="Net">
           <TreeItem nodeId="51" label="inoutnet">
             <Graphbase usession={props} urlt="inoutnet"/>
           </TreeItem>
           <TreeItem nodeId="52" label="Net Budget">
             <Graphbase usession={props} urlt="netbudget"/>
           </TreeItem>
         </TreeItem>
      </TreeItem>
    </TreeView>
  );
}

export default Tv