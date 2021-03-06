/*
 * Copyright (c) 2014 Swen Walkowski.
 * All rights reserved. Originator: Swen Walkowski.
 * Get more information about CardDAVSyncOutlook at https://github.com/somedevelopment/CardDAVSyncOutlook/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package outlook;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import utilities.LegacyCorrectionUtilities;
import main.Status;

import com.jacob.com.Dispatch;

import contact.Contact;
import contact.Contacts;
import contact.Contacts.Addressbook;

public class ManageOutlookContacts extends ManageOutlook<Contact, Contacts> {

    /**
     * Constructors
     */
    public ManageOutlookContacts(String strWorkingDir, int intOutlookFolder) {
        super(strWorkingDir, intOutlookFolder);
    }

    /**
     *
     * Private Section
     *
     */
    private String downloadContactPicture(Dispatch dipContact, String strWorkingDir) {
        String strPathToTmpPicture = null;

        Dispatch dipAttachments = Dispatch.get(dipContact, "Attachments").toDispatch();
        @SuppressWarnings("deprecation")
        int countAttachements = Dispatch.call((Dispatch) dipAttachments, "Count").toInt();
        for (int j = 1; j <= countAttachements; j++) {
            Dispatch currentAttachement;
            currentAttachement = Dispatch.call(dipAttachments, "Item", j).toDispatch();

            if (Dispatch.get(currentAttachement, "FileName").toString().equals("ContactPicture.jpg")) {
                strPathToTmpPicture = strWorkingDir + Math.random() + ".jpg";

                //The Crashing Part
                Dispatch.call(currentAttachement, "SaveAsFile", strPathToTmpPicture);
            }
            currentAttachement.safeRelease();
        }
        dipAttachments.safeRelease();

        return strPathToTmpPicture;
    }

    /**
     *
     * Interface Implementation Section
     *
     */
    @Override
    public void loadContentFromOutlook(Contacts contacts) {

        Dispatch dipContactsFolder = Dispatch.call(ManageOutlook.dipNamespace, "GetDefaultFolder", (Object) super.intOutlookFolder).toDispatch();
        Dispatch dipContactItems = Dispatch.get(dipContactsFolder, "items").toDispatch();

        @SuppressWarnings("deprecation")
        int count = Dispatch.call(dipContactItems, "Count").toInt();

        for (int i = 1; i <= count; i++) {
            Dispatch dipContact;
            dipContact = Dispatch.call(dipContactItems, "Item", i).toDispatch();

            // user1 field is used for storing the CardDAV UID in Outlook
            String strUid = Dispatch.get(dipContact, "User1").toString().trim();

            String strEntryID = Dispatch.get(dipContact, "EntryID").toString().trim();

            String strTitle = Dispatch.get(dipContact, "Title").toString().trim();
            String strFirstName = Dispatch.get(dipContact, "FirstName").toString().trim();
            String strMiddleName = Dispatch.get(dipContact, "MiddleName").toString().trim();
            String strLastName = Dispatch.get(dipContact, "LastName").toString().trim();
            String strSuffix = Dispatch.get(dipContact, "Suffix").toString().trim();

            String strCompanyName = Dispatch.get(dipContact, "CompanyName").toString().trim();

            String strJobTitle = Dispatch.get(dipContact, "JobTitle").toString().trim();

            String strEmail1Address = Dispatch.get(dipContact, "Email1Address").toString().trim();
            String strEmail2Address = Dispatch.get(dipContact, "Email2Address").toString().trim();
            String strEmail3Address = Dispatch.get(dipContact, "Email3Address").toString().trim();

            String strWebPage = Dispatch.get(dipContact, "WebPage").toString().trim();

            String strMobileTelephoneNumber = Dispatch.get(dipContact, "MobileTelephoneNumber").toString().trim();
            String strAssistantTelephoneNumber = Dispatch.get(dipContact, "AssistantTelephoneNumber").toString().trim();
            String strCallbackTelephoneNumber = Dispatch.get(dipContact, "CallbackTelephoneNumber").toString().trim();
            String strCarTelephoneNumber = Dispatch.get(dipContact, "CarTelephoneNumber").toString().trim();
            String strCompanyMainTelephoneNumber = Dispatch.get(dipContact, "CompanyMainTelephoneNumber").toString().trim();
            String strOtherTelephoneNumber = Dispatch.get(dipContact, "OtherTelephoneNumber").toString().trim();
            String strPrimaryTelephoneNumber = Dispatch.get(dipContact, "PrimaryTelephoneNumber").toString().trim();
            String strRadioTelephoneNumber = Dispatch.get(dipContact, "RadioTelephoneNumber").toString().trim();
            String strTTYTDDTelephoneNumber = Dispatch.get(dipContact, "TTYTDDTelephoneNumber").toString().trim();

            String strBusinessTelephoneNumber = Dispatch.get(dipContact, "BusinessTelephoneNumber").toString().trim();
            String strBusiness2TelephoneNumber = Dispatch.get(dipContact, "Business2TelephoneNumber").toString().trim();
            String strBusinessFaxNumber = Dispatch.get(dipContact, "BusinessFaxNumber").toString().trim();

            String strHomeTelephoneNumber = Dispatch.get(dipContact, "HomeTelephoneNumber").toString().trim();
            String strHome2TelephoneNumber = Dispatch.get(dipContact, "Home2TelephoneNumber").toString().trim();
            String strHomeFaxNumber = Dispatch.get(dipContact, "HomeFaxNumber").toString().trim();

            String strHomeAddressCity = Dispatch.get(dipContact, "HomeAddressCity").toString().trim();
            String strHomeAddressCountry = Dispatch.get(dipContact, "HomeAddressCountry").toString().trim();
            String strHomeAddressPostalCode = Dispatch.get(dipContact, "HomeAddressPostalCode").toString().trim();
            String strHomeAddressState = Dispatch.get(dipContact, "HomeAddressState").toString().trim();
            String strHomeAddressStreet = Dispatch.get(dipContact, "HomeAddressStreet").toString().trim();

            String strBusinessAddressCity = Dispatch.get(dipContact, "BusinessAddressCity").toString().trim();
            String strBusinessAddressCountry = Dispatch.get(dipContact, "BusinessAddressCountry").toString().trim();
            String strBusinessAddressPostalCode = Dispatch.get(dipContact, "BusinessAddressPostalCode").toString().trim();
            String strBusinessAddressState = Dispatch.get(dipContact, "BusinessAddressState").toString().trim();
            String strBusinessAddressStreet = Dispatch.get(dipContact, "BusinessAddressStreet").toString().trim();

            String strBody = Dispatch.get(dipContact, "Body").toString().trim();

            SimpleDateFormat dataFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);
            Calendar calBirthday = null;
            Calendar calAnniversary = null;
            try {
                String strBirthday = Dispatch.get(dipContact, "Birthday").toString();
                String strAnniversary = Dispatch.get(dipContact, "Anniversary").toString();

                calBirthday = Calendar.getInstance();
                calBirthday.setTime(dataFormat.parse(strBirthday));

                calAnniversary = Calendar.getInstance();
                calAnniversary.setTime(dataFormat.parse(strAnniversary));

                Date tmpDate = calAnniversary.getTime();
                Date noDateOutlook = dataFormat.parse("Sat Jan 01 00:00:00 CET 4501");
                if (tmpDate.getTime() == noDateOutlook.getTime()) {
                    calAnniversary = null;
                }

                tmpDate = calBirthday.getTime();
                if (tmpDate.getTime() == noDateOutlook.getTime()) {
                    calBirthday = null;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }

            String strPathToTmpPicture = downloadContactPicture(dipContact, strWorkingDir);

            String strLastModificationTime = Dispatch.get(dipContact, "LastModificationTime").toString().trim();

            //Add Contact
            contacts.addContact(Addressbook.OUTLOOKADDRESSBOOK, new Contact(strUid, strEntryID, strTitle, strFirstName, strMiddleName, strLastName, strSuffix,
                    strCompanyName, strJobTitle, strEmail1Address, strEmail2Address, strEmail3Address,
                    strWebPage, strMobileTelephoneNumber, strAssistantTelephoneNumber, strCallbackTelephoneNumber,
                    strCarTelephoneNumber, strCompanyMainTelephoneNumber, strOtherTelephoneNumber,
                    strPrimaryTelephoneNumber, strRadioTelephoneNumber, strTTYTDDTelephoneNumber, strBusinessTelephoneNumber,
                    strBusiness2TelephoneNumber, strBusinessFaxNumber, strHomeTelephoneNumber, strHome2TelephoneNumber, strHomeFaxNumber, strHomeAddressCity,
                    strHomeAddressCountry, strHomeAddressPostalCode, strHomeAddressState, strHomeAddressStreet,
                    strBusinessAddressCity, strBusinessAddressCountry, strBusinessAddressPostalCode, strBusinessAddressState,
                    strBusinessAddressStreet, strBody, calBirthday, calAnniversary, strPathToTmpPicture, strLastModificationTime));

            Status.print("Load Outlook Contact " + strFirstName + ", " + strLastName);

            dipContact.safeRelease();
        }
        dipContactsFolder.safeRelease();
        dipContactItems.safeRelease();
    }

    @Override
    public void writeOutlookObjects(Contacts contacts) {

        List<Contact> listDelOutlookContacts = new ArrayList();

        for (Entry<String, Contact> currentOutlookEntry : contacts.getAddressbook(Addressbook.OUTLOOKADDRESSBOOK).entrySet()) {

        	//Legacy correction UID call
            if (LegacyCorrectionUtilities.bodyHasUID(currentOutlookEntry.getValue().getBody())) {
                currentOutlookEntry.getValue().setBody(LegacyCorrectionUtilities.cleanBodyFromUID(currentOutlookEntry.getValue().getBody()));
                if ((currentOutlookEntry.getValue().getStatus() == Contact.Status.READIN) ||
                        (currentOutlookEntry.getValue().getStatus() == Contact.Status.UIDADDED) ||
                        (currentOutlookEntry.getValue().getStatus() == Contact.Status.UNCHANGED)) {
                    currentOutlookEntry.getValue().setStatus(Contact.Status.CHANGED);
                }
            }

            //Correction of numbers INTERNATIONAL formating
            if (contacts.getCorrectNumber()) {
                currentOutlookEntry.getValue().correctNumbers(contacts.getDefaultRegion());
                if ((currentOutlookEntry.getValue().getStatus() == Contact.Status.READIN) ||
                        (currentOutlookEntry.getValue().getStatus() == Contact.Status.UIDADDED) ||
                        (currentOutlookEntry.getValue().getStatus() == Contact.Status.UNCHANGED)) {
                    currentOutlookEntry.getValue().setStatus(Contact.Status.CHANGED);
                }
            }


            switch (currentOutlookEntry.getValue().getStatus()) {
                case UIDADDED:
                    Status.print("Write Contact with new UID to Outlook " +
                            currentOutlookEntry.getValue().getFirstName() + ", " +
                            currentOutlookEntry.getValue().getLastName());
                    super.updateOutlookItem(generatePutDispatchContent(currentOutlookEntry.getValue()));
                    break;
                case CHANGED:
                    Status.print("Write Changed Contact to Outlook " +
                            currentOutlookEntry.getValue().getFirstName() + ", " +
                            currentOutlookEntry.getValue().getLastName());
                    super.updateOutlookItem(generatePutDispatchContent(currentOutlookEntry.getValue()));
                    break;
                case NEW:
                    Status.print("Write New Contact to Outlook " +
                            currentOutlookEntry.getValue().getFirstName() + ", " +
                            currentOutlookEntry.getValue().getLastName());
                    currentOutlookEntry.getValue().setEntryID(super.getNewOutlookItem());
                    super.updateOutlookItem(generatePutDispatchContent(currentOutlookEntry.getValue()));
                    break;
                case DELETE:
                    Status.print("Delete Contact in Outlook " +
                            currentOutlookEntry.getValue().getFirstName() + ", " +
                            currentOutlookEntry.getValue().getLastName());
                    super.deletOutlookItem(currentOutlookEntry.getValue().getEntryID());
                    listDelOutlookContacts.add(currentOutlookEntry.getValue());
                    break;
                case READIN:
                    //Nothing to do
                    break;
                case UNCHANGED:
                    //Nothing to do
                    break;
            }
        }

        //Delete deleted Contacts
        for (Contact currentContact : listDelOutlookContacts) {
            contacts.removeContact(Addressbook.OUTLOOKADDRESSBOOK, currentContact.getUid());
        }

    }

    @Override
    protected Dispatch generatePutDispatchContent(Contact dataItem) {
        Contact dataContact = (Contact) dataItem;
        Dispatch dipContact = super.getOutlookItem(dataContact.getEntryID());
        SimpleDateFormat dataFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.ENGLISH);

        Dispatch.put(dipContact, "User1", dataContact.getUid());

        if (dataContact.getTitle() != null) {
            Dispatch.put(dipContact, "Title", dataContact.getTitle());
        } else {
            Dispatch.put(dipContact, "Title", "");
        }

        if (dataContact.getFirstName() != null) {
            Dispatch.put(dipContact, "FirstName", dataContact.getFirstName());
        } else {
            Dispatch.put(dipContact, "FirstName", "");
        }

        if (dataContact.getMiddleName() != null) {
            Dispatch.put(dipContact, "MiddleName", dataContact.getMiddleName());
        } else {
            Dispatch.put(dipContact, "MiddleName", "");
        }

        if (dataContact.getLastName() != null) {
            Dispatch.put(dipContact, "LastName", dataContact.getLastName());
        } else {
            Dispatch.put(dipContact, "LastName", "");
        }

        if (dataContact.getSuffix() != null) {
            Dispatch.put(dipContact, "Suffix", dataContact.getSuffix());
        } else {
            Dispatch.put(dipContact, "Suffix", "");
        }

        if (dataContact.getCompanyName() != null) {
            Dispatch.put(dipContact, "CompanyName", dataContact.getCompanyName());
        } else {
            Dispatch.put(dipContact, "CompanyName", "");
        }

        if (dataContact.getJobTitle() != null) {
            Dispatch.put(dipContact, "JobTitle", dataContact.getJobTitle());
        } else {
            Dispatch.put(dipContact, "JobTitle", "");
        }

        if (dataContact.getEmail1Address() != null) {
            Dispatch.put(dipContact, "Email1Address", dataContact.getEmail1Address());
        } else {
            Dispatch.put(dipContact, "Email1Address", "");
        }

        if (dataContact.getEmail2Address() != null) {
            Dispatch.put(dipContact, "Email2Address", dataContact.getEmail2Address());
        } else {
            Dispatch.put(dipContact, "Email2Address", "");
        }

        if (dataContact.getEmail3Address() != null) {
            Dispatch.put(dipContact, "Email3Address", dataContact.getEmail3Address());
        } else {
            Dispatch.put(dipContact, "Email3Address", "");
        }

        if (dataContact.getWebPage() != null) {
            Dispatch.put(dipContact, "WebPage", dataContact.getWebPage());
        } else {
            Dispatch.put(dipContact, "WebPage", "");
        }

        if (dataContact.getMobileTelephoneNumber() != null) {
            Dispatch.put(dipContact, "MobileTelephoneNumber", dataContact.getMobileTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "MobileTelephoneNumber", "");
        }

        if (dataContact.getBusinessTelephoneNumber() != null) {
            Dispatch.put(dipContact, "BusinessTelephoneNumber", dataContact.getBusinessTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "BusinessTelephoneNumber", "");
        }

        if (dataContact.getBusiness2TelephoneNumber() != null) {
            Dispatch.put(dipContact, "Business2TelephoneNumber", dataContact.getBusiness2TelephoneNumber());
        } else {
            Dispatch.put(dipContact, "Business2TelephoneNumber", "");
        }

        if (dataContact.getBusinessFaxNumber() != null) {
            Dispatch.put(dipContact, "BusinessFaxNumber", dataContact.getBusinessFaxNumber());
        } else {
            Dispatch.put(dipContact, "BusinessFaxNumber", "");
        }

        if (dataContact.getHomeTelephoneNumber() != null) {
            Dispatch.put(dipContact, "HomeTelephoneNumber", dataContact.getHomeTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "HomeTelephoneNumber", "");
        }

        if (dataContact.getHome2TelephoneNumber() != null) {
            Dispatch.put(dipContact, "Home2TelephoneNumber", dataContact.getHome2TelephoneNumber());
        } else {
            Dispatch.put(dipContact, "Home2TelephoneNumber", "");
        }

        if (dataContact.getHomeFaxNumber() != null) {
            Dispatch.put(dipContact, "HomeFaxNumber", dataContact.getHomeFaxNumber());
        } else {
            Dispatch.put(dipContact, "HomeFaxNumber", "");
        }

        if (dataContact.getHomeAddressCity() != null) {
            Dispatch.put(dipContact, "HomeAddressCity", dataContact.getHomeAddressCity());
        } else {
            Dispatch.put(dipContact, "HomeAddressCity", "");
        }

        if (dataContact.getHomeAddressCountry() != null) {
            Dispatch.put(dipContact, "HomeAddressCountry", dataContact.getHomeAddressCountry());
        } else {
            Dispatch.put(dipContact, "HomeAddressCountry", "");
        }

        if (dataContact.getHomeAddressPostalCode() != null) {
            Dispatch.put(dipContact, "HomeAddressPostalCode", dataContact.getHomeAddressPostalCode());
        } else {
            Dispatch.put(dipContact, "HomeAddressPostalCode", "");
        }

        if (dataContact.getHomeAddressState() != null) {
            Dispatch.put(dipContact, "HomeAddressState", dataContact.getHomeAddressState());
        } else {
            Dispatch.put(dipContact, "HomeAddressState", "");
        }

        if (dataContact.getHomeAddressStreet() != null) {
            Dispatch.put(dipContact, "HomeAddressStreet", dataContact.getHomeAddressStreet());
        } else {
            Dispatch.put(dipContact, "HomeAddressStreet", "");
        }

        if (dataContact.getBusinessAddressCity() != null) {
            Dispatch.put(dipContact, "BusinessAddressCity", dataContact.getBusinessAddressCity());
        } else {
            Dispatch.put(dipContact, "BusinessAddressCity", "");
        }

        if (dataContact.getBusinessAddressCountry() != null) {
            Dispatch.put(dipContact, "BusinessAddressCountry", dataContact.getBusinessAddressCountry());
        } else {
            Dispatch.put(dipContact, "BusinessAddressCountry", "");
        }

        if (dataContact.getBusinessAddressPostalCode() != null) {
            Dispatch.put(dipContact, "BusinessAddressPostalCode", dataContact.getBusinessAddressPostalCode());
        } else {
            Dispatch.put(dipContact, "BusinessAddressPostalCode", "");
        }

        if (dataContact.getBusinessAddressState() != null) {
            Dispatch.put(dipContact, "BusinessAddressState", dataContact.getBusinessAddressState());
        } else {
            Dispatch.put(dipContact, "BusinessAddressState", "");
        }

        if (dataContact.getBusinessAddressStreet() != null) {
            Dispatch.put(dipContact, "BusinessAddressStreet", dataContact.getBusinessAddressStreet());
        } else {
            Dispatch.put(dipContact, "BusinessAddressStreet", "");
        }

        if (dataContact.getBody() != null) {
            Dispatch.put(dipContact, "Body", dataContact.getBody());
        } else {
            Dispatch.put(dipContact, "Body", "");
        }

        if (dataContact.getBirthday() != null) {
            Dispatch.put(dipContact, "Birthday", dataContact.getBirthday());
        } else {
            try {
                Date noDateOutlook = dataFormat.parse("Sat Jan 01 00:00:00 CET 4501");
                Dispatch.put(dipContact, "Birthday", noDateOutlook);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dataContact.getAnniversary() != null) {
            Dispatch.put(dipContact, "Anniversary", dataContact.getAnniversary());
        } else {
            try {
                Date noDateOutlook = dataFormat.parse("Sat Jan 01 00:00:00 CET 4501");
                Dispatch.put(dipContact, "Anniversary", noDateOutlook);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dataContact.getAssistantTelephoneNumber() != null) {
            Dispatch.put(dipContact, "AssistantTelephoneNumber", dataContact.getAssistantTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "AssistantTelephoneNumber", "");
        }

        if (dataContact.getCallbackTelephoneNumber() != null) {
            Dispatch.put(dipContact, "CallbackTelephoneNumber", dataContact.getCallbackTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "CallbackTelephoneNumber", "");
        }

        if (dataContact.getCarTelephoneNumber() != null) {
            Dispatch.put(dipContact, "CarTelephoneNumber", dataContact.getCarTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "CarTelephoneNumber", "");
        }

        if (dataContact.getCompanyMainTelephoneNumber() != null) {
            Dispatch.put(dipContact, "CompanyMainTelephoneNumber", dataContact.getCompanyMainTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "CompanyMainTelephoneNumber", "");
        }

        if (dataContact.getOtherTelephoneNumber() != null) {
            Dispatch.put(dipContact, "OtherTelephoneNumber", dataContact.getOtherTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "OtherTelephoneNumber", "");
        }

        if (dataContact.getPrimaryTelephoneNumber() != null) {
            Dispatch.put(dipContact, "PrimaryTelephoneNumber", dataContact.getPrimaryTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "PrimaryTelephoneNumber", "");
        }

        if (dataContact.getRadioTelephoneNumber() != null) {
            Dispatch.put(dipContact, "RadioTelephoneNumber", dataContact.getRadioTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "RadioTelephoneNumber", "");
        }

        if (dataContact.getTTYTDDTelephoneNumber() != null) {
            Dispatch.put(dipContact, "TTYTDDTelephoneNumber", dataContact.getTTYTDDTelephoneNumber());
        } else {
            Dispatch.put(dipContact, "TTYTDDTelephoneNumber", "");
        }

        if (dataContact.getPathToContactPicture() != null) {
            Dispatch.call(dipContact, "AddPicture", dataContact.getPathToContactPicture());
        }

        return dipContact;
    }

    //TODO
    public void listContactFolders() {

        //Dispatch dipRootFolder = Dispatch.call(ManageOutlook.dipNamespace, "GetRootFolder").toDispatch();
        Dispatch dipRootFolder = Dispatch.call(ManageOutlook.dipNamespace, "Folders").toDispatch();
        Dispatch dipFolders = Dispatch.get(dipRootFolder, "Folders").toDispatch();

        int count = Dispatch.call(dipFolders, "Count").getInt();
        for (int i = 1; i <= count; i++) {

            Dispatch dipFolder = Dispatch.call(dipFolders, "Folder", i).toDispatch();

            // user1 field is used for storing the CardDAV UID in Outlook
            String strName = Dispatch.get(dipFolder, "Name").toString().trim();
            String strMessageClass = Dispatch.get(dipFolder, "DefaultMessageClass").toString().trim();
            System.out.println("TODO: "+i+" "+strName+" "+strMessageClass);
        }
    }
}
