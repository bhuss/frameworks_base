/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.nfc.tech;

import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;

/**
 * Provides access to tags containing just a barcode.
 *
 * <p>Acquire an {@link NfcBarcode} object using {@link #get}.
 *
 */
public final class NfcBarcode extends BasicTagTechnology {

    /** Kovio Tags */
    public static final int TYPE_KOVIO = 1;
    public static final int TYPE_UNKNOWN = -1;

    /** @hide */
    public static final String EXTRA_BARCODE_TYPE = "barcodetype";

    private int mType;

    /**
     * Get an instance of {@link NfcBarcode} for the given tag.
     *
     * <p>Returns null if {@link NfcBarcode} was not enumerated in {@link Tag#getTechList}.
     *
     * <p>Does not cause any RF activity and does not block.
     *
     * @param tag an NfcBarcode compatible tag
     * @return NfcBarcode object
     */
    public static NfcBarcode get(Tag tag) {
        if (!tag.hasTech(TagTechnology.NFC_BARCODE)) return null;
        try {
            return new NfcBarcode(tag);
        } catch (RemoteException e) {
            return null;
        }
    }

    /**
     * Internal constructor, to be used by NfcAdapter
     * @hide
     */
    public NfcBarcode(Tag tag) throws RemoteException {
        super(tag, TagTechnology.NFC_BARCODE);
        Bundle extras = tag.getTechExtras(TagTechnology.NFC_BARCODE);
        if (extras != null) {
            mType = extras.getInt(EXTRA_BARCODE_TYPE);
        } else {
            throw new NullPointerException("NfcBarcode tech extras are null.");
        }
    }

    /**
     * Returns the NFC Barcode tag type.
     *
     * <p>Currently only one of {@link #TYPE_KOVIO} or {@link #TYPE_UNKNOWN}.
     *
     * <p>Does not cause any RF activity and does not block.
     *
     * @return the NFC Barcode tag type
     */
    public int getType() {
        return mType;
    }

    /**
     * Returns the barcode of an NfcBarcode tag.
     *
     * <p>Does not cause any RF activity and does not block.
     *
     * @return a byte array containing the barcode
     */
    public byte[] getBarcode() {
        switch (mType) {
            case TYPE_KOVIO:
                // For Kovio tags the barcode matches the ID
                return mTag.getId();
            default:
                return null;
        }
    }
}
