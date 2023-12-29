import React from 'react';

import TreeView from '@material-ui/lab/TreeView';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import ChevronRightIcon from '@material-ui/icons/ChevronRight';
import TreeItem from '@material-ui/lab/TreeItem';
import FData from './FData';
import Graphbase from './Graphbase';
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
import { transferColumns } from './tables/TransferTable';
import { inoutnetColumns } from './tables/InOutNetTable';
import { balanceColumns } from './tables/BalanceTable';
import { stypeDataColumns } from './tables/StypeDataTable';
import { billsColumns } from './tables/BillsTable';
import { annualColumns } from './tables/AnnualTable';
import { utilsColumns } from './tables/UtilsTable';
import { creditpaidColumns } from './tables/CreditPaidTable';
import { categoriesColumns } from './tables/CategoriesTable';

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
        <TreeItem nodeId="7" label="Stypes">
          <FData usession={props} columns={stypeDataColumns} title="Stypes" urlt="stype" jsonp="stype"/>
        </TreeItem>
        <TreeItem nodeId="8" label="Bills">
          <FData usession={props} columns={billsColumns} title="Bills" urlt="bills" jsonp="bills"/>
        </TreeItem>
        <TreeItem nodeId="9" label="Statements">
          <FData usession={props} columns={statementColumns} title="Statements" urlt="statements" jsonp="statement"/>
        </TreeItem>
        <TreeItem nodeId="10" label="Annual">
          <FData usession={props} columns={annualColumns} title="Annual" urlt="annual" jsonp="annual"/>
        </TreeItem>
        <TreeItem nodeId="11" label="Utils">
          <FData usession={props} columns={utilsColumns} title="Utils" urlt="utils" jsonp="utils"/>
        </TreeItem>
        <TreeItem nodeId="12" label="CreditPaid">
          <FData usession={props} columns={creditpaidColumns} title="CreditPaid" urlt="creditp" jsonp="credit"/>
        </TreeItem>
        <TreeItem nodeId="13" label="CreditSpent">
          <FData usession={props} columns={creditpaidColumns} title="CreditSpent" urlt="credits" jsonp="credit"/>
        </TreeItem>
        <TreeItem nodeId="31" label="Categories">
          <FData usession={props} columns={categoriesColumns} title="Categories" urlt="categories" jsonp="categories"/>
        </TreeItem>
      </TreeItem>
      <TreeItem nodeId="5" label="Graphs">
        <TreeItem nodeId="41" label="SType">
          <TreeItem nodeId="25" label="stypeAtm">
            <Graphbase usession={props} urlt="stype/atm"/>
          </TreeItem>
          <TreeItem nodeId="26" label="stypeBills">
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
        <TreeItem nodeId="34" label="electric">
           <Graphbase usession={props} urlt="electric"/>
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
         <TreeItem nodeId="39" label="Dog">
           <Graphbase usession={props} urlt="dog"/>
         </TreeItem>
         <TreeItem nodeId="40" label="ML">
            <Graphbase usession={props} urlt="ml"/>
         </TreeItem>
         <TreeItem nodeId="42" label="Credit">
           <TreeItem nodeId="43" label="Capital One">
             <Graphbase usession={props} urlt="capone"/>
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
           <Graphbase usession={props} urlt="budget"/>
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